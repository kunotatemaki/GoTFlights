package com.rookia.gotflights.usecases

import com.rookia.gotflights.data.repository.Repository
import com.rookia.gotflights.domain.network.Flight
import com.rookia.gotflights.domain.network.Inbound
import com.rookia.gotflights.domain.network.Outbound
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import org.junit.Assert.assertEquals


class GetFlightsUseCaseTest {

    private val fligth1 = getFlight("Valladolid", "Madrid", 12.toBigDecimal())
    private val fligth2 = getFlight("Zamora", "Sevilla", 1.toBigDecimal())
    private val fligth3 = getFlight("Valladolid", "Madrid", 11.toBigDecimal())
    private val fligth4 = getFlight("Zamora", "Sevilla", 10.toBigDecimal())
    private val fligth5 = getFlight("Valladolid", "Madrid", 10.toBigDecimal())

    private val flights = listOf(fligth1, fligth2, fligth3, fligth4, fligth5)

    private val repository: Repository = mockk()
    private val getFlightsUseCase = GetFlightsUseCase(repository)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getFlights() {
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