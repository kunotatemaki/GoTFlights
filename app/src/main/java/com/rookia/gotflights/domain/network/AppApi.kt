package com.rookia.gotflights.domain.network

import retrofit2.Response
import retrofit2.http.GET

interface AppApi {

    @GET(".")
    suspend fun getFlights(): Response<FlightApiResponse>


    //region NETWORK RESPONSE CLASSES
    // TODO: 16/01/2020 remove this
    class Products constructor(val products: List<Item>)
    class Item(
        val code: String,
        val name: String,
        val price: Double
    )

    //endregion
}
