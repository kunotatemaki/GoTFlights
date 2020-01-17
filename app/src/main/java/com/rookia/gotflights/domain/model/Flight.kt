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
    private var exchangeRate: BigDecimal? = 1.toBigDecimal()

    fun getConvertedPrice(): BigDecimal? {
        if (price != null) {
            exchangeRate?.let {
                return price * it
            }
        }
        return null
    }

    fun getConvertedPriceFormatted(): String {
        getConvertedPrice()?.let {
            var formattedText = "${formatDecimalValue(it)}$targetCurrencyName"
            if (currency != targetCurrencyName) {
                formattedText += "  ($price$currency)"
            }
            return formattedText
        }
        return ""
    }

    fun setExchangeRate(rate: BigDecimal?) {
        exchangeRate = rate
    }

    fun needToConvert() = currency != targetCurrencyName

    fun isValidPrice() = exchangeRate != null && price != null
}