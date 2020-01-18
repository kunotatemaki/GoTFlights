package com.rookia.gotflights.ui.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.vo.Result
import com.rookia.gotflights.usecases.GetFlightsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

open class FlightsViewModel constructor(
    private val getFlightsUseCase: GetFlightsUseCase
) : ViewModel() {

    val flights: MediatorLiveData<Result<List<Flight>>> = MediatorLiveData()

    var maxPrice: BigDecimal? = null
    private set
    var minPrice: BigDecimal? = null
    private set

    @VisibleForTesting
    val fetchTrigger = MutableLiveData<Long>()

    init {
        flights.addSource(fetchTrigger) {
            val flightsFromRepo = getFlightsUseCase.getFlights()
            flights.addSource(flightsFromRepo) {
                flightsFromRepo.value?.let {
                    viewModelScope.launch {
                        val remove = formatFlightsAndSendItToTheView(it)
                        if(remove) flights.removeSource(flightsFromRepo)
                    }
                }
            }
        }
        fetchTrigger.value = 0
    }

    /**
     * this function prepares the data to be shown on the screen
     * @param data as it comes from the server -> a list of flights wrapped in the result class
     * @return true if the source has to be removed from the mediator
     */
    @VisibleForTesting
    suspend fun formatFlightsAndSendItToTheView(result: Result<List<Flight>>): Boolean =
        withContext(Dispatchers.Default) {
            val sameCurrencyList = getFlightsUseCase.convertToSameCurrency(result.data)
            val formattedList = getFlightsUseCase.orderByPriceAndRemoveDuplicates(sameCurrencyList)
            storeMaxAndMinPrices(formattedList)
            when (result.status) {
                Result.Status.SUCCESS -> {
                    flights.postValue(Result.success(formattedList))
                    true
                }
                Result.Status.ERROR -> {
                    flights.postValue(Result.error(result.message, formattedList))
                    true
                }
                Result.Status.LOADING -> {
                    flights.postValue(Result.loading(formattedList))
                    false
                }
            }
        }

    @VisibleForTesting
    fun storeMaxAndMinPrices(orderedList: List<Flight>){
        minPrice = orderedList.firstOrNull()?.convertedPrice
        maxPrice = orderedList.lastOrNull()?.convertedPrice
    }

    fun refresh() {
        fetchTrigger.value = System.currentTimeMillis()
    }


}
