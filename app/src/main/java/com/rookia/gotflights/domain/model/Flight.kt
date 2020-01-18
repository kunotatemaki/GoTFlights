package com.rookia.gotflights.domain.model

import com.rookia.gotflights.utils.formatDecimalValue
import java.math.BigDecimal

data class Flight(
    val currency: String?,
    val inbound: Inbound?,
    val outbound: Outbound?,
    private val price: BigDecimal?,
    private val targetCurrencyName: String
) {
    private var exchangeRate: BigDecimal? = null
    var convertedPrice: BigDecimal? = null
    private set

    fun setExchangeRate(rate: BigDecimal?) {
        convertedPrice = null
        exchangeRate = rate
        if (price != null) {
            exchangeRate?.let {
                convertedPrice = price * it
            }
        }
    }

    fun getConvertedPriceFormatted(): String {
        convertedPrice?.let {
            var formattedText = "${formatDecimalValue(it)}$targetCurrencyName"
            if (currency != targetCurrencyName) {
                formattedText += " (${formatDecimalValue(price)}$currency)"
            }
            return formattedText
        }
        return ""
    }

    fun needToConvert() = currency != targetCurrencyName

    fun isValidPrice() = exchangeRate != null && price != null
}