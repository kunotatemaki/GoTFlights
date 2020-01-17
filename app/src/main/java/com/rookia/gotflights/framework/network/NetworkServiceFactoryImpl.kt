package com.rookia.gotflights.framework.network

import com.rookia.gotflights.domain.network.AppApi
import com.rookia.gotflights.data.network.NetworkServiceFactory
import com.rookia.gotflights.data.network.NetworkServiceFactory.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkServiceFactoryImpl @Inject constructor(): NetworkServiceFactory {

    @Volatile
    private var instance: AppApi? = null

    override fun getServiceInstance(): AppApi =
        instance ?: buildNetworkService().also { instance = it }

    private fun buildNetworkService(): AppApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(AppApi::class.java)

}