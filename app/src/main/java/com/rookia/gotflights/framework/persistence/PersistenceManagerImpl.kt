package com.rookia.gotflights.framework.persistence

import com.rookia.gotflights.data.persistence.PersistenceManager
import com.rookia.gotflights.domain.network.model.ExchangeRate
import com.rookia.gotflights.framework.persistence.databases.AppDatabase
import com.rookia.gotflights.framework.persistence.entities.ExchangeRateEntity
import com.rookia.gotflights.framework.persistence.entities.mapper.ExchangeRateEntityMapper
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersistenceManagerImpl @Inject constructor(
    private val db: AppDatabase
) : PersistenceManager {

    override suspend fun getExchangeRate(from: String, to: String): ExchangeRateEntity? {
        val relation = ExchangeRateEntityMapper.getRelationName(from, to)
        return db.exchangeRateDao().getExchangeRate(relation)
    }

    override suspend fun storeExchangeRate(
        originalCurrency: String,
        exchangeRate: ExchangeRate?,
        date: Date
    ) {
        exchangeRate?.let {
            db.exchangeRateDao()
                .insert(ExchangeRateEntityMapper.map(originalCurrency, it, date))
        }
    }
}

