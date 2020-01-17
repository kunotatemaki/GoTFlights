package com.rookia.gotflights.data.repository

import androidx.lifecycle.LiveData
import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.network.model.ApiResponse
import com.rookia.gotflights.domain.network.model.ExchangeRate
import com.rookia.gotflights.domain.vo.Result

interface Repository {

    fun getFlights(targetCurrencyName: String): LiveData<Result<List<Flight>>>
    fun getFlightsFromCache(): LiveData<List<Flight>>
    suspend fun getFlightsFromNetwork(): Result<ApiResponse>
    suspend fun getExchangeRate(from: String, to: String): Result<ExchangeRate>

}