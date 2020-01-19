package com.rookia.gotflights.data.persistence

import com.rookia.gotflights.domain.network.model.ExchangeRate
import com.rookia.gotflights.framework.persistence.entities.ExchangeRateEntity
import java.util.*

interface PersistenceManager {

    suspend fun getExchangeRate(from: String, to: String): ExchangeRateEntity?
    suspend fun storeExchangeRate(originalCurrency: String, exchangeRate: ExchangeRate?, date: Date)

}