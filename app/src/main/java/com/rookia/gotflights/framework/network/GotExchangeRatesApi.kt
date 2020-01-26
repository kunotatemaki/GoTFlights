package com.rookia.gotflights.framework.network

import com.rookia.gotflights.domain.network.model.ExchangeRate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GotExchangeRatesApi {

    @GET("currency")
    suspend fun getExchangeRate(
        @Query("from") from: String,
        @Query("to") to: String
    ): Response<ExchangeRate>

}
