package com.rookia.gotflights.framework.repository


import androidx.lifecycle.LiveData
import com.rookia.gotflights.data.network.NetworkServiceFactory
import com.rookia.gotflights.data.repository.Repository
import com.rookia.gotflights.domain.model.FlightsCache
import com.rookia.gotflights.domain.network.Flight
import com.rookia.gotflights.domain.network.FlightApiResponse
import com.rookia.gotflights.domain.vo.Result
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RepositoryImpl @Inject constructor(
    private val networkServiceFactory: NetworkServiceFactory,
    private val flightsCache: FlightsCache
) :
    Repository {

    override fun getFlights(): LiveData<Result<List<Flight>>> =
        resultLiveData(
            persistedDataQuery = {
                getFlightsFromCache()
            },
            networkCall = {
                getFlightsFromNetwork()
            },
            persistCallResult = {
                saveToCache(it?.results ?: listOf())
            },
            runNetworkCall = true
        )

    override fun getFlightsFromCache(): LiveData<List<Flight>> =
        flightsCache.getListOfFlightsInCache()

    override suspend fun getFlightsFromNetwork(): Result<FlightApiResponse> {
        return try {
            val resp = networkServiceFactory.getServiceInstance().getFlights()
            if (resp.isSuccessful && resp.body() != null) {
                Result.success(resp.body())
            } else {
                Result.error(resp.message())
            }
        } catch (e: Exception) {
            Result.error("error fetching from network")
        }
    }

    private fun saveToCache(flights: List<Flight>) {
        flightsCache.saveListOfFlightsInCache(flights)
    }

}