package com.rookia.gotflights.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat


fun formatDecimalValue(value: BigDecimal?, removeDecimalsIfZero: Boolean = false): String {
    value ?: return ""
    val df = if (removeDecimalsIfZero) {
        DecimalFormat("0.##")
    } else {
        DecimalFormat("0.00")
    }
    df.roundingMode = RoundingMode.DOWN
    return df.format(value)
}