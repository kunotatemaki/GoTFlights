package com.rookia.gotflights.ui.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.vo.Result
import com.rookia.gotflights.usecases.GetFlightsUseCase

open class FlightsViewModel constructor(
    private val getFlightsUseCase: GetFlightsUseCase
) : ViewModel() {

    val flights: LiveData<Result<List<Flight>>>
    get() = Transformations.distinctUntilChanged(field)

    @VisibleForTesting
    val fetchTrigger = MutableLiveData<Long>()

    init {
        flights = fetchTrigger.switchMap {
            getFlightsUseCase.getFlights()
        }
        fetchTrigger.value = 0
    }


    fun refresh() {
        fetchTrigger.value = System.currentTimeMillis()
    }


}
