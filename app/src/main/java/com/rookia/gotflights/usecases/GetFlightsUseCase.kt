package com.rookia.gotflights.usecases

import androidx.lifecycle.LiveData
import com.rookia.gotflights.data.repository.Repository
import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.vo.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFlightsUseCase constructor(private val repository: Repository) {

    private val targetCurrencyName = "EUR"

    fun getFlights(): LiveData<Result<List<Flight>>> =
        repository.getFlights(targetCurrencyName)

    suspend fun convertToSameCurrency(list: List<Flight>?): List<Flight> =
        withContext(Dispatchers.IO) {
            val listOfCurrencies =
                list?.filterNot { it.currency == targetCurrencyName }
                    ?.mapNotNull { it.currency }?.distinct()
            val mapOfExchangeRates = mutableMapOf<String, Double?>()
            listOfCurrencies?.forEach { currency ->
                val exchangeRate =
                    repository.getExchangeRate(from = currency, to = targetCurrencyName)
                if (exchangeRate != null) {
                    mapOfExchangeRates[currency] = exchangeRate.value
                }
            }
            list?.forEach { flight ->
                if (flight.needToConvert().not()) {
                    flight.setExchangeRate(1.toDouble())
                } else {
                    flight.setExchangeRate(mapOfExchangeRates[flight.currency])
                }
            }
            list?.filter { it.isValidPrice() } ?: listOf()
        }

    fun orderByPriceAndRemoveDuplicates(list: List<Flight>?): List<Flight> =
        list?.sortedBy {
            it.convertedPrice
        }?.distinctBy { "${it.inbound?.origin}${it.inbound?.destination}" } ?: listOf()

}