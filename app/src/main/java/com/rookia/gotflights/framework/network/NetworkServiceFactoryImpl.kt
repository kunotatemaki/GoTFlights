package com.rookia.gotflights.framework.network

import com.rookia.gotflights.domain.network.GotFlightsApi
import com.rookia.gotflights.data.network.NetworkServiceFactory
import com.rookia.gotflights.data.network.NetworkServiceFactory.Companion.FLIGHTS_BASE_URL
import com.rookia.gotflights.data.network.NetworkServiceFactory.Companion.EXCHANGE_RATES_BASE_URL
import com.rookia.gotflights.domain.network.GotExchangeRatesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkServiceFactoryImpl @Inject constructor(): NetworkServiceFactory {

    @Volatile
    private var flightsInstance: GotFlightsApi? = null

    @Volatile
    private var exchangeRatesInstance: GotExchangeRatesApi? = null

    override fun getFlightsServiceInstance(): GotFlightsApi =
        flightsInstance ?: buildFlightsNetworkService().also { flightsInstance = it }

    override fun getExchangeRatesServiceInstance(): GotExchangeRatesApi =
        exchangeRatesInstance ?: buildExchangeRatesNetworkService().also { exchangeRatesInstance = it }

    private fun buildFlightsNetworkService(): GotFlightsApi = Retrofit.Builder()
        .baseUrl(FLIGHTS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GotFlightsApi::class.java)

    private fun buildExchangeRatesNetworkService(): GotExchangeRatesApi = Retrofit.Builder()
        .baseUrl(EXCHANGE_RATES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GotExchangeRatesApi::class.java)

}