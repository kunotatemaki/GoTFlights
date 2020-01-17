package com.rookia.gotflights.domain.model

import androidx.lifecycle.MutableLiveData
import com.rookia.gotflights.domain.model.mapper.ApiToCacheMapper
import com.rookia.gotflights.domain.network.model.FlightApiResponse


class FlightsCache {

    private var listOfFlights: MutableLiveData<List<Flight>> = MutableLiveData()

    init {
        listOfFlights.value = listOf()
    }

    @Synchronized
    fun getListOfFlightsInCache() = listOfFlights

    @Synchronized
    fun saveListOfFlightsInCache(list: List<FlightApiResponse>, targetCurrencyName: String) {
        listOfFlights.postValue(list.map { ApiToCacheMapper.map(it, targetCurrencyName) })
    }

}