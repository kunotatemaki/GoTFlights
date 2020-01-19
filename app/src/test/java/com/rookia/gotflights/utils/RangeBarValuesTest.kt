package com.rookia.gotflights.utils

import org.junit.Test

import org.junit.Assert.*


class RangeBarValuesTest {

    @Test
    fun `next value (lower than 10)`() {
        val value = 8.toBigDecimal()
        assertEquals(10.toBigInteger(), RangeBarValues.getHigherValue(value))
    }

    @Test
    fun `next value (higher than 10)`() {
        val value = 879.toBigDecimal()
        assertEquals(880.toBigInteger(), RangeBarValues.getHigherValue(value))
    }

    @Test
    fun `prev value (lower than 10)`() {
        val value = 8.toBigDecimal()
        assertEquals(0.toBigInteger(), RangeBarValues.getLowerValue(value))
    }

    @Test
    fun `prev value (higher than 10)`() {
        val value = 879.toBigDecimal()
        assertEquals(870.toBigInteger(), RangeBarValues.getLowerValue(value))
    }

    @Test
    fun `get interval `() {
        assertEquals(2.toBigInteger(), RangeBarValues.getInterval(142.toBigInteger(), 5.toBigInteger()))
    }

    @Test
    fun `get interval (result less that 1)`() {
        assertEquals(1.toBigInteger(), RangeBarValues.getInterval(7.toBigInteger(), 5.toBigInteger()))
    }

    @Test
    fun `get pin Value`() {
        assertEquals(27, RangeBarValues.getPinValue(7f, 5.0, 4))
    }
}