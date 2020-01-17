package com.rookia.gotflights.data.network

import com.rookia.gotflights.domain.network.GotExchangeRatesApi
import com.rookia.gotflights.domain.network.GotFlightsApi

interface NetworkServiceFactory {

    companion object {
        const val FLIGHTS_BASE_URL = "https://odigeo-testandroid.herokuapp.com/"
        const val EXCHANGE_RATES_BASE_URL = "http://jarvisstark.herokuapp.com/"

    }

    fun getFlightsServiceInstance(): GotFlightsApi
    fun getExchangeRatesServiceInstance(): GotExchangeRatesApi
}

