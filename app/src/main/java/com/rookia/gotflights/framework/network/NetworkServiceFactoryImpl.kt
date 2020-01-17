package com.rookia.gotflights.framework.network

import com.rookia.gotflights.domain.network.GotFlightsApi
import com.rookia.gotflights.data.network.NetworkServiceFactory
import com.rookia.gotflights.data.network.NetworkServiceFactory.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkServiceFactoryImpl @Inject constructor(): NetworkServiceFactory {

    @Volatile
    private var instance: GotFlightsApi? = null

    override fun getServiceInstance(): GotFlightsApi =
        instance ?: buildNetworkService().also { instance = it }

    private fun buildNetworkService(): GotFlightsApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GotFlightsApi::class.java)

}