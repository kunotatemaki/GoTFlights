package com.rookia.gotflights.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rookia.gotflights.data.repository.Repository
import com.rookia.gotflights.domain.network.Flight
import com.rookia.gotflights.domain.network.Inbound
import com.rookia.gotflights.domain.network.Outbound
import com.rookia.gotflights.domain.vo.Result
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import testclasses.getItem
import java.math.BigDecimal


class GetFlightsUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val flight1 = getFlight("Valladolid", "Madrid", 12.toBigDecimal())
    private val flight2 = getFlight("Zamora", "Sevilla", 1.toBigDecimal())
    private val flight3 = getFlight("Valladolid", "Madrid", 11.toBigDecimal())
    private val flight4 = getFlight("Zamora", "Sevilla", 10.toBigDecimal())
    private val flight5 = getFlight("Valladolid", "Madrid", 10.toBigDecimal())

    private val flights = listOf(flight1, flight2, flight3, flight4, flight5)

    private val repository: Repository = mockk()
    private val getFlightsUseCase = GetFlightsUseCase(repository)

    @Test
    fun getFlightsWithSuccessResponse() {
        every { repository.getFlights() } returns MutableLiveData<Result<List<Flight>>>().also {
            it.postValue(Result.success(flights))
        }
        val flights = getFlightsUseCase.getFlights()
        val response = flights.getItem()
        assertEquals(Result.Status.SUCCESS, response.status)
        assertEquals(2, response.data!!.size)
    }

    @Test
    fun getFlightsWithErrorResponse() {
        val message = "error message"
        every { repository.getFlights() } returns MutableLiveData<Result<List<Flight>>>().also {
            it.postValue(Result.error(message, flights))
        }
        val flights = getFlightsUseCase.getFlights()
        val response = flights.getItem()
        assertEquals(Result.Status.ERROR, response.status)
        assertEquals(message, response.message)
        assertEquals(2, response.data!!.size)
    }

    @Test
    fun getFlightsWithLoadingResponse() {
        every { repository.getFlights() } returns MutableLiveData<Result<List<Flight>>>().also {
            it.postValue(Result.loading())
        }
        val flights = getFlightsUseCase.getFlights()
        val response = flights.getItem()
        assertEquals(Result.Status.LOADING, response.status)
        assertEquals(0, response.data!!.size)
    }

    @Test
    fun orderByPriceAndRemoveDuplicates() {
        val orderedList = getFlightsUseCase.orderByPriceAndRemoveDuplicates(flights)
        assertEquals(2, orderedList.size)
        assertEquals(10.toBigDecimal(), orderedList.last().price)
        assertEquals("Valladolid", orderedList.last().inbound?.origin)
        assertEquals(1.toBigDecimal(), orderedList.first().price)
        assertEquals("Zamora", orderedList.first().inbound?.origin)
    }

    private fun getFlight(origin: String, destination: String, price: BigDecimal): Flight =
        Flight(
            currency = "EUR",
            inbound = Inbound(
                airline = "airline",
                airlineImage = "image",
                arrivalDate = "20/02/1902",
                arrivalTime = "20:08",
                departureDate = "20/02/1902",
                departureTime = "20:08",
                origin = origin,
                destination = destination
            ),
            outbound = Outbound(
                airline = "airline",
                airlineImage = "image",
                arrivalDate = "20/02/1902",
                arrivalTime = "20:08",
                departureDate = "20/02/1902",
                departureTime = "20:08",
                origin = destination,
                destination = origin
            ),
            price = price
        )
}