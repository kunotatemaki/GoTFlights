package com.rookia.gotflights.domain.network

import retrofit2.Response
import retrofit2.http.GET

interface AppApi {

    @GET(".")
    suspend fun getFlights(): Response<FlightApiResponse>

}
