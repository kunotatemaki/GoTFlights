package com.rookia.gotflights.utils

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.roundToInt


object RangeBarValues {

    fun getLowerValue(min: BigDecimal): BigInteger {
        val rounded = min.toBigInteger()
        return rounded - (rounded % 10.toBigInteger())
    }

    fun getHigherValue(max: BigDecimal): BigInteger {
        val rounded = max.toBigInteger()
        return rounded + (10.toBigInteger() - (rounded % 10.toBigInteger()))
    }

    fun getInterval(max: BigInteger, min: BigInteger): BigInteger {
        val interval: BigInteger = (max - min) / 50.toBigInteger()
        return if (interval < 1.toBigInteger()) 1.toBigInteger() else interval
    }

    fun getPinValue(startValue: Float, interval: Double, index: Int): Int =
        (startValue + interval * index).roundToInt()
}