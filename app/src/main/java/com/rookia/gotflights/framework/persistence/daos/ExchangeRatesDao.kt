package com.rookia.gotflights.framework.persistence.daos

import androidx.room.Dao
import androidx.room.Query
import com.rookia.gotflights.framework.persistence.entities.ExchangeRateEntity


@Dao
abstract class ExchangeRatesDao : BaseDao<ExchangeRateEntity>() {

    @Query("SELECT * FROM exchange_rate WHERE relation = :relation")
    abstract fun getExchangeRate(relation: String): ExchangeRateEntity?

}