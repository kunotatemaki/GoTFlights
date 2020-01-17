package com.rookia.gotflights.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class MoneyFormatUtilsKtTest {

    @Test
    fun `format non zero decimal value with show decimals flag`() {
        val decimal = 10.5.toBigDecimal()
        assertEquals("10,50", formatDecimalValue(decimal, false))
    }

    @Test
    fun `format non zero decimal value with hide decimals flag`() {
        val decimal = 10.5.toBigDecimal()
        assertEquals("10,5", formatDecimalValue(decimal, true))
    }

    @Test
    fun `format zero decimal value with show decimals flag`() {
        val decimal = 10.0.toBigDecimal()
        assertEquals("10,00", formatDecimalValue(decimal, false))
    }

    @Test
    fun `format zero decimal value with hide decimals flag`() {
        val decimal = 10.0.toBigDecimal()
        assertEquals("10", formatDecimalValue(decimal, true))
    }
}