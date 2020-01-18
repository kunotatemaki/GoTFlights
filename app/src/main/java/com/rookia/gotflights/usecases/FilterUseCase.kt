package com.rookia.gotflights.usecases

import com.rookia.gotflights.domain.model.Flight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal


class FilterUseCase {

    suspend fun filterListOfFlights(
        list: List<Flight>,
        minPrice: BigDecimal,
        maxPrice: BigDecimal
    ): List<Flight> = withContext(Dispatchers.Default) {
        list.filter { it.convertedPrice != null }
            .filter { it.convertedPrice!! >= minPrice }
            .filter { it.convertedPrice!! <= maxPrice }
    }
}