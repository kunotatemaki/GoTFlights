package com.rookia.gotflights.utils

import org.junit.Test

import org.junit.Assert.*


class RangeBarValuesTest {

    @Test
    fun `next value (lower than 10)`() {
        val value = 8.toDouble()
        assertEquals(10, RangeBarValues.getHigherValue(value))
    }

    @Test
    fun `next value (higher than 10)`() {
        val value = 879.toDouble()
        assertEquals(880, RangeBarValues.getHigherValue(value))
    }

    @Test
    fun `prev value (lower than 10)`() {
        val value = 8.toDouble()
        assertEquals(0, RangeBarValues.getLowerValue(value))
    }

    @Test
    fun `prev value (higher than 10)`() {
        val value = 879.toDouble()
        assertEquals(870, RangeBarValues.getLowerValue(value))
    }

    @Test
    fun `get interval `() {
        assertEquals(2, RangeBarValues.getInterval(142, 5))
    }

    @Test
    fun `get interval (result less that 1)`() {
        assertEquals(1, RangeBarValues.getInterval(7, 5))
    }

    @Test
    fun `get pin Value`() {
        assertEquals(27, RangeBarValues.getPinValue(7f, 5.0, 4))
    }
}