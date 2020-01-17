package com.rookia.gotflights.domain.model

import androidx.lifecycle.MutableLiveData
import com.rookia.gotflights.domain.network.Flight


class FlightsCache {

    private var listOfFlights: MutableLiveData<List<Flight>> = MutableLiveData()
    init {
        listOfFlights.value = listOf()
    }

    @Synchronized
    fun getListOfFlightsInCache() = listOfFlights

    @Synchronized
    fun saveListOfFlightsInCache(list: List<Flight>) {
        listOfFlights.postValue(list)
    }

}