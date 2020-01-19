package com.rookia.gotflights.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rookia.gotflights.data.repository.Repository
import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.network.model.ExchangeRate
import com.rookia.gotflights.domain.vo.Result
import com.rookia.gotflights.framework.persistence.entities.mapper.ExchangeRateEntityMapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import testclasses.getFlight
import testclasses.getItem
import java.util.*


@Suppress("BlockingMethodInNonBlockingContext")
class GetFlightsUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val flight1 = getFlight(
        "Valladolid",
        "Madrid",
        12.toDouble()
    ).also { it.setExchangeRate(1.toDouble()) }
    private val flight2 = getFlight(
        "Zamora",
        "Sevilla",
        1.toDouble()
    ).also { it.setExchangeRate(1.toDouble()) }
    private val flight3 = getFlight(
        "Valladolid",
        "Madrid",
        11.toDouble()
    ).also { it.setExchangeRate(1.toDouble()) }
    private val flight4 = getFlight(
        "Zamora",
        "Sevilla",
        10.toDouble()
    ).also { it.setExchangeRate(1.toDouble()) }
    private val flight5 = getFlight(
        "Valladolid",
        "Madrid",
        10.toDouble()
    ).also { it.setExchangeRate(1.toDouble()) }

    private val flightEUR = getFlight("Coruña", "Zaragoza", 10.toDouble(), "EUR")
    private val flightGBP = getFlight("Zamora", "Sevilla", 10.toDouble(), "GBP")
    private val flightUSD = getFlight("Valladolid", "Madrid", 10.toDouble(), "USD")

    private val flights = listOf(flight1, flight2, flight3, flight4, flight5)

    private val repository: Repository = mockk()
    private val getFlightsUseCase = GetFlightsUseCase(repository)

    @Test
    fun getFlightsWithSuccessResponse() {
        every {
            repository.getFlights(any())
        } returns MutableLiveData<Result<List<Flight>>>().also {
            it.postValue(Result.success(flights))
        }
        runBlocking {
            val flights = getFlightsUseCase.getFlights()
            val response = flights.getItem()
            val list = response.data!!
            val currencyList = getFlightsUseCase.convertToSameCurrency(list)
            val finalList = getFlightsUseCase.orderByPriceAndRemoveDuplicates(currencyList)
            assertEquals(Result.Status.SUCCESS, response.status)
            assertEquals(2, finalList.size)
        }
    }

    @Test
    fun getFlightsWithErrorResponse() {
        val message = "error message"
        every {
            repository.getFlights(any())
        } returns MutableLiveData<Result<List<Flight>>>().also {
            it.postValue(Result.error(message, flights))
        }
        runBlocking {
            val flights = getFlightsUseCase.getFlights()
            val response = flights.getItem()
            val list = response.data!!
            val currencyList = getFlightsUseCase.convertToSameCurrency(list)
            val finalList = getFlightsUseCase.orderByPriceAndRemoveDuplicates(currencyList)
            assertEquals(Result.Status.ERROR, response.status)
            assertEquals(message, response.message)
            assertEquals(2, finalList.size)
        }
    }

    @Test
    fun getFlightsWithLoadingResponse() {
        every {
            repository.getFlights(any())
        } returns MutableLiveData<Result<List<Flight>>>().also {
            it.postValue(Result.loading())
        }
        runBlocking {
            val flights = getFlightsUseCase.getFlights()
            val response = flights.getItem()
            val list = response.data
            val currencyList = getFlightsUseCase.convertToSameCurrency(list)
            val finalList = getFlightsUseCase.orderByPriceAndRemoveDuplicates(currencyList)
            assertEquals(Result.Status.LOADING, response.status)
            assertEquals(0, finalList.size)
        }
    }

    @Test
    fun orderByPriceAndRemoveDuplicates() {
        val orderedList = getFlightsUseCase.orderByPriceAndRemoveDuplicates(flights)
        assertEquals(2, orderedList.size)
        assertEquals(10.toDouble(), orderedList.last().convertedPrice)
        assertEquals("Valladolid", orderedList.last().inbound?.origin)
        assertEquals(1.toDouble(), orderedList.first().convertedPrice)
        assertEquals("Zamora", orderedList.first().inbound?.origin)
    }

    @Test
    fun `convert to same currency a list of EUR, GBP and USD`() {
        runBlocking(Dispatchers.IO) {
            val date = Date()
            coEvery { repository.getExchangeRate(from = "GBP", to = "EUR") } returns
                    ExchangeRateEntityMapper.map("GBP", ExchangeRate("EUR", 1.5), date)

            coEvery { repository.getExchangeRate(from = "USD", to = "EUR") } returns
                    ExchangeRateEntityMapper.map("GBP", ExchangeRate("EUR", 0.8), date)

            val list =
                getFlightsUseCase.convertToSameCurrency(listOf(flightEUR, flightGBP, flightUSD))
            assertEquals(10.toDouble(), list.first().convertedPrice)
            assertEquals("10,00EUR", list.first().getConvertedPriceFormatted())
            assertEquals(15.0.toDouble(), list[1].convertedPrice)
            assertEquals("15,00EUR (10,00GBP)", list[1].getConvertedPriceFormatted())
            assertEquals(8.0.toDouble(), list.last().convertedPrice)
            assertEquals("8,00EUR (10,00USD)", list.last().getConvertedPriceFormatted())
        }
    }

    @Test
    fun `convert to same currency a list of EUR, GBP and USD removing wrong values`() {

        runBlocking(Dispatchers.IO) {
            val date = Date()
            coEvery { repository.getExchangeRate(from = "GBP", to = "EUR") } returns
                    ExchangeRateEntityMapper.map("GBP", ExchangeRate("EUR", 1.5), date)

            coEvery { repository.getExchangeRate(from = "USD", to = "EUR") } returns
                    ExchangeRateEntityMapper.map("GBP", ExchangeRate("EUR", 0.8), date)
            val list =
                getFlightsUseCase.convertToSameCurrency(listOf(flightEUR, flightGBP, flightUSD))
            assertEquals(3, list.size)
            assertEquals(10.toDouble(), list.first().convertedPrice)
            assertEquals(8.toDouble(), list.last().convertedPrice)
        }
    }


}