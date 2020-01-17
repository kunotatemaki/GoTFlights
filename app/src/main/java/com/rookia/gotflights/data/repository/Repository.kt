package com.rookia.gotflights.data.repository

import androidx.lifecycle.LiveData
import com.rookia.gotflights.domain.network.Flight
import com.rookia.gotflights.domain.network.FlightApiResponse
import com.rookia.gotflights.domain.vo.Result

interface Repository {

    fun getFlights(): LiveData<Result<List<Flight>>>
    fun getFlightsFromCache(): LiveData<List<Flight>>
    suspend fun getFlightsFromNetwork(): Result<FlightApiResponse>

}