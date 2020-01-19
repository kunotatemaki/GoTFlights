package com.rookia.gotflights.usecases

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import testclasses.getFlight


class FilterUseCaseTest {

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
        9.toDouble()
    ).also { it.setExchangeRate(1.toDouble()) }
    private val flight5 = getFlight(
        "Valladolid",
        "Madrid",
        10.toDouble()
    ).also { it.setExchangeRate(1.toDouble()) }

    private val filterUseCase: FilterUseCase = FilterUseCase()

    @Test
    fun `filter a list of flights excluding from the beginning and from the end`() {
        runBlocking {
            val filteredList = filterUseCase.filterListOfFlights(
                listOf(flight1, flight2, flight3, flight4, flight5),
                2.toDouble(),
                11.toDouble()
            ).sortedBy { it.convertedPrice }
            assertEquals(3, filteredList.size)
            assertEquals(9.toDouble(), filteredList.first().convertedPrice)
            assertEquals(11.toDouble(), filteredList.last().convertedPrice)
        }
    }

    @Test
    fun `filter a list of flights excluding only from the beginning`() {
        runBlocking {
            val filteredList = filterUseCase.filterListOfFlights(
                listOf(flight1, flight2, flight3, flight4, flight5),
                10.toDouble(),
                20.toDouble()
            ).sortedBy { it.convertedPrice }
            assertEquals(3, filteredList.size)
            assertEquals(10.toDouble(), filteredList.first().convertedPrice)
            assertEquals(12.toDouble(), filteredList.last().convertedPrice)
        }
    }

    @Test
    fun `filter a list of flights excluding only from the end`() {
        runBlocking {
            val filteredList = filterUseCase.filterListOfFlights(
                listOf(flight1, flight2, flight3, flight4, flight5),
                0.toDouble(),
                9.toDouble()
            ).sortedBy { it.convertedPrice }
            assertEquals(2, filteredList.size)
            assertEquals(1.toDouble(), filteredList.first().convertedPrice)
            assertEquals(9.toDouble(), filteredList.last().convertedPrice)
        }
    }

    @Test
    fun `filter a list of flights including all`() {
        runBlocking {
            val filteredList = filterUseCase.filterListOfFlights(
                listOf(flight1, flight2, flight3, flight4, flight5),
                0.toDouble(),
                20.toDouble()
            ).sortedBy { it.convertedPrice }
            assertEquals(5, filteredList.size)
            assertEquals(1.toDouble(), filteredList.first().convertedPrice)
            assertEquals(12.toDouble(), filteredList.last().convertedPrice)
        }
    }

    @Test
    fun `filter an empty list`() {
        runBlocking {
            val filteredList = filterUseCase.filterListOfFlights(
                listOf(),
                2.toDouble(),
                11.toDouble()
            ).sortedBy { it.convertedPrice }
            assertTrue(filteredList.isEmpty())
        }
    }
}