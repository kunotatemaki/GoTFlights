package com.rookia.gotflights.domain.network

import com.rookia.gotflights.domain.network.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface GotFlightsApi {

    @GET(".")
    suspend fun getFlights(): Response<ApiResponse>

}
