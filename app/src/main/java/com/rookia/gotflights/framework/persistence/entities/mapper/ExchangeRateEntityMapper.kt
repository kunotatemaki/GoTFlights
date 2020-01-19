package com.rookia.gotflights.framework.persistence.entities.mapper

import com.rookia.gotflights.domain.network.model.ExchangeRate
import com.rookia.gotflights.framework.persistence.entities.ExchangeRateEntity
import java.util.*


object ExchangeRateEntityMapper {
    fun map(originalCurrency: String, exchangeRate: ExchangeRate, date: Date): ExchangeRateEntity =
        ExchangeRateEntity(
            relation = getRelationName(
                from = originalCurrency,
                to = exchangeRate.currency ?: ""
            ),
            value = exchangeRate.exchangeRate,
            date = date,
            to = exchangeRate.currency,
            from = originalCurrency
        )


    fun getRelationName(from: String, to: String): String = "$from-$to"
}