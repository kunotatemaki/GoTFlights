package com.rookia.gotflights.usecases

import com.rookia.gotflights.domain.model.Flight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FilterUseCase {

    suspend fun filterListOfFlights(
        list: List<Flight>,
        minPrice: Double,
        maxPrice: Double
    ): List<Flight> = withContext(Dispatchers.Default) {
        list.filter { it.convertedPrice != null }
            .filter { it.convertedPrice!! >= minPrice }
            .filter { it.convertedPrice!! <= maxPrice }
    }
}