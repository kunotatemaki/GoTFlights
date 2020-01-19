package com.rookia.gotflights.ui.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.vo.Result
import com.rookia.gotflights.usecases.FilterUseCase
import com.rookia.gotflights.usecases.GetFlightsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class FlightsViewModel constructor(
    private val getFlightsUseCase: GetFlightsUseCase,
    private val filterUseCase: FilterUseCase
) : ViewModel() {

    val flights: MediatorLiveData<Result<List<Flight>>> = MediatorLiveData()
    private var listOfFlights: List<Flight> = listOf()

    private val filterShownMutable = MutableLiveData<Boolean>()
    val filterShown: LiveData<Boolean> = filterShownMutable

    var maxPrice: Double? = null
        private set
    var minPrice: Double? = null
        private set

    @VisibleForTesting
    val fetchTrigger = MutableLiveData<Long>()

    init {
        flights.addSource(fetchTrigger) {
            maxPrice = null
            minPrice = null
            val flightsFromRepo = getFlightsUseCase.getFlights()
            flights.addSource(flightsFromRepo) {
                flightsFromRepo.value?.let {
                    viewModelScope.launch {
                        listOfFlights = formatFlights(it)
                        if (it.status != Result.Status.LOADING) {
                            flights.removeSource(flightsFromRepo)
                        }
                        flights.value = Result(it.status, listOfFlights, it.message)
                    }
                }
            }
        }

        fetchTrigger.value = 0
    }

    @VisibleForTesting
    suspend fun formatFlights(result: Result<List<Flight>>): List<Flight> =
        withContext(Dispatchers.Default) {
            val sameCurrencyList = getFlightsUseCase.convertToSameCurrency(result.data)
            getFlightsUseCase.orderByPriceAndRemoveDuplicates(sameCurrencyList).also {
                storeMaxAndMinPrices(it)
            }
        }

    @VisibleForTesting
    fun storeMaxAndMinPrices(orderedList: List<Flight>) {
        minPrice = orderedList.firstOrNull()?.convertedPrice
        maxPrice = orderedList.lastOrNull()?.convertedPrice
    }

    fun refresh() {
        fetchTrigger.value = System.currentTimeMillis()
    }

    fun filterFlights(minVal: Float, maxVal: Float) =
        viewModelScope.launch {
            flights.value = Result.loading(flights.value?.data)
            val filteredList = filterUseCase.filterListOfFlights(listOfFlights, minVal.toDouble(), maxVal.toDouble())
            flights.value = Result.success(filteredList)
        }

    fun canShowFilter(): Boolean = maxPrice != null && minPrice != null

    fun showFilter(){
        filterShownMutable.value = true
    }

    fun hideFilter(){
        filterShownMutable.value = false
    }


}
