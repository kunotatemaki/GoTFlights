package com.rookia.gotflights.data.repository

import androidx.lifecycle.LiveData
import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.network.model.ApiResponse
import com.rookia.gotflights.domain.network.model.ExchangeRate
import com.rookia.gotflights.domain.vo.Result
import com.rookia.gotflights.framework.persistence.entities.ExchangeRateEntity

interface Repository {

    fun getFlights(targetCurrencyName: String): LiveData<Result<List<Flight>>>
    suspend fun getExchangeRate(from: String, to: String): ExchangeRateEntity?

}