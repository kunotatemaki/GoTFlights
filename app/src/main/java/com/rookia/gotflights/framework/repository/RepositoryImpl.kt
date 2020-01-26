package com.rookia.gotflights.framework.repository


import androidx.lifecycle.LiveData
import com.rookia.gotflights.data.persistence.PersistenceManager
import com.rookia.gotflights.data.repository.Repository
import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.model.FlightsCache
import com.rookia.gotflights.domain.network.model.ApiResponse
import com.rookia.gotflights.domain.network.model.ExchangeRate
import com.rookia.gotflights.domain.network.model.FlightApiResponse
import com.rookia.gotflights.domain.vo.Result
import com.rookia.gotflights.framework.network.NetworkServiceFactory
import com.rookia.gotflights.framework.persistence.entities.ExchangeRateEntity
import com.rookia.gotflights.utils.DateUtils
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RepositoryImpl @Inject constructor(
    private val networkServiceFactory: NetworkServiceFactory,
    private val flightsCache: FlightsCache,
    private val persistenceManager: PersistenceManager
) :
    Repository {

    override fun getFlights(targetCurrencyName: String): LiveData<Result<List<Flight>>> =
        resultLiveData(
            persistedDataQuery = {
                getFlightsFromCache()
            },
            networkCall = {
                getFlightsFromNetwork()
            },
            persistCallResult = {
                saveFlightsToCache(it?.results ?: listOf(), targetCurrencyName)
            },
            runNetworkCall = true
        )

    private fun getFlightsFromCache(): LiveData<List<Flight>> =
        flightsCache.getListOfFlightsInCache()

    private suspend fun getFlightsFromNetwork(): Result<ApiResponse> =
        try {
            val api = networkServiceFactory.getFlightsServiceInstance()
            val resp = api.getFlights()
            if (resp.isSuccessful && resp.body() != null) {
                Result.success(resp.body())
            } else {
                Result.error(resp.message())
            }
        } catch (e: Exception) {
            Result.error("error fetching from network")
        }

    private fun saveFlightsToCache(flights: List<FlightApiResponse>, targetCurrencyName: String) {
        flightsCache.saveListOfFlightsInCache(flights, targetCurrencyName)
    }

    override suspend fun getExchangeRate(from: String, to: String): ExchangeRateEntity? =
        resultRefusingOutdatedInfo<ExchangeRateEntity, ExchangeRate>(
            persistedDataQuery = {
                getExchangeRateFromCache(from = from, to = to)
            },
            networkCall = {
                getExchangeRateFromNetwork(from = from, to = to)
            },
            persistCallResult = {
                saveExchangeRateToCache(
                    originalCurrency = from,
                    exchangeRate = it,
                    date = Date(System.currentTimeMillis())
                )
            },
            isThePersistedInfoOutdated = {
                needToGetExchangeRateInfo(it)
            }
        )


    private suspend fun getExchangeRateFromCache(from: String, to: String): ExchangeRateEntity? =
        persistenceManager.getExchangeRate(from = from, to = to)

    private suspend fun getExchangeRateFromNetwork(from: String, to: String): Result<ExchangeRate> =
        try {
            val resp =
                networkServiceFactory.getExchangeRatesServiceInstance().getExchangeRate(from, to)
            if (resp.isSuccessful && resp.body() != null) {
                Result.success(resp.body())
            } else {
                Result.error(resp.message())
            }
        } catch (e: Exception) {
            Result.error("error fetching from network")
        }

    private suspend fun saveExchangeRateToCache(
        originalCurrency: String,
        exchangeRate: ExchangeRate?,
        date: Date
    ) {
        persistenceManager.storeExchangeRate(originalCurrency, exchangeRate, date)
    }

    private fun needToGetExchangeRateInfo(entity: ExchangeRateEntity?): Boolean =
        DateUtils.isToday(entity?.date).not()



//        try {
//            val resp = networkServiceFactory.getExchangeRatesServiceInstance().getExchangeRate(from, to)
//            if (resp.isSuccessful && resp.body() != null) {
//                Result.success(resp.body())
//            } else {
//                Result.error(resp.message())
//            }
//        } catch (e: Exception) {
//            Result.error("error fetching from network")
//        }


}