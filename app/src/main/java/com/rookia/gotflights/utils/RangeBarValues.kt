package com.rookia.gotflights.utils

import kotlin.math.roundToInt


object RangeBarValues {

    fun getLowerValue(min: Double): Int {
        val rounded = min.toInt()
        return rounded - (rounded % 10)
    }

    fun getHigherValue(max: Double): Int {
        val rounded = max.toInt()
        return rounded + (10 - (rounded % 10))
    }

    fun getInterval(max: Int, min: Int): Int {
        val interval: Int = (max - min) / 50
        return if (interval < 1) 1 else interval
    }

    fun getPinValue(startValue: Float, interval: Double, index: Int): Int =
        (startValue + interval * index).roundToInt()
}