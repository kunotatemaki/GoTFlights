package com.rookia.gotflights.data.network

import com.rookia.gotflights.domain.network.GotFlightsApi

interface NetworkServiceFactory {

    companion object {
        const val BASE_URL = "https://odigeo-testandroid.herokuapp.com/"

    }

    fun getServiceInstance(): GotFlightsApi
}

