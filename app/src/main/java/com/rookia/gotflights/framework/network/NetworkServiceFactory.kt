package com.rookia.gotflights.framework.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


open class NetworkServiceFactory {

    companion object {
        const val FLIGHTS_BASE_URL = "https://odigeo-testandroid.herokuapp.com/"
        const val EXCHANGE_RATES_BASE_URL = "http://jarvisstark.herokuapp.com/"

    }

    @Volatile
    private var flightsInstance: GotFlightsApi? = null

    @Volatile
    private var exchangeRatesInstance: GotExchangeRatesApi? = null

    open fun getFlightsServiceInstance(): GotFlightsApi =
        flightsInstance ?: buildFlightsNetworkService().also { flightsInstance = it }

    open fun getExchangeRatesServiceInstance(): GotExchangeRatesApi =
        exchangeRatesInstance ?: buildExchangeRatesNetworkService().also {
            exchangeRatesInstance = it
        }

    private fun buildFlightsNetworkService(): GotFlightsApi = Retrofit.Builder()
        .baseUrl(FLIGHTS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GotFlightsApi::class.java)

    private fun buildExchangeRatesNetworkService(): GotExchangeRatesApi = Retrofit.Builder()
        .baseUrl(EXCHANGE_RATES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GotExchangeRatesApi::class.java)

}