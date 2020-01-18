package com.rookia.gotflights


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rookia.gotflights.domain.model.FlightsCache
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import testclasses.getFlightFromNetwork
import testclasses.getItem

@Suppress("BlockingMethodInNonBlockingContext")
class LocalCacheTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var flightsCache: FlightsCache? = null

    private val networkFlight1 = getFlightFromNetwork("Valladolid", "Zamora", 1.toBigDecimal())
    private val networkFlight2 = getFlightFromNetwork("Valencia", "Madrid", 11.toBigDecimal())
    private val networkFlight3 = getFlightFromNetwork("Barcelona", "Sevilla", 31.toBigDecimal())

    @Before
    @Throws(Exception::class)
    fun setUp() {
        flightsCache = FlightsCache()

    }

    @After
    @Throws(Exception::class)
    fun close() {
        flightsCache = null
    }

    @Test
    @Throws(InterruptedException::class)
    fun testCacheEmpty() {
        val flights = flightsCache!!.getListOfFlightsInCache().getItem()
        assertTrue(flights.isEmpty())
    }

    @Test
    @Throws(InterruptedException::class)
    fun testDataInCache() {
        val flightsToStore = listOf(networkFlight1, networkFlight2, networkFlight3)
        flightsCache!!.saveListOfFlightsInCache(flightsToStore, "EUR")
        val flights = flightsCache!!.getListOfFlightsInCache().getItem()
        assertEquals(3, flights.size)
    }


}