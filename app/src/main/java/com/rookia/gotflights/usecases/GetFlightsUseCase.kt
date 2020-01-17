package com.rookia.gotflights.usecases

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.rookia.gotflights.data.repository.Repository
import com.rookia.gotflights.domain.network.Flight
import com.rookia.gotflights.domain.vo.Result

class GetFlightsUseCase constructor(private val repository: Repository) {

    fun getFlights(): LiveData<Result<List<Flight>>> =
        repository.getFlights().switchMap { flights ->
            val list = flights.data
            val status = flights.status
            val message = flights.message
//            val orderedList =
//                list?.sortedWith(
//                    compareBy(
//                        { "${it.inbound?.origin}${it.inbound?.destination}" },
//                        { it.price}
//                    )
//                )?.distinctBy {  "${it.inbound?.origin}${it.inbound?.destination}"  }

            val orderedList = orderByPriceAndRemoveDuplicates(list)

            val result = when (status) {
                Result.Status.SUCCESS -> Result.success(orderedList)
                Result.Status.ERROR -> Result.error(message, orderedList)
                Result.Status.LOADING -> Result.loading(orderedList)
            }
            val liveData = MutableLiveData<Result<List<Flight>>>()
            liveData.postValue(result)
            liveData
        }

    @VisibleForTesting
    fun orderByPriceAndRemoveDuplicates(list: List<Flight>?): List<Flight> =
        list?.sortedBy {
            it.price
        }?.distinctBy {  "${it.inbound?.origin}${it.inbound?.destination}"  } ?: listOf()

}