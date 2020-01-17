package com.rookia.gotflights.data.network

import com.rookia.gotflights.domain.network.AppApi

interface NetworkServiceFactory {

    companion object {
        const val BASE_URL = "https://odigeo-testandroid.herokuapp.com/"

    }

    fun getServiceInstance(): AppApi
}

