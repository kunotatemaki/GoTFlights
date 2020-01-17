package com.rookia.gotflights.domain.model

import com.rookia.gotflights.data.resources.ResourcesManager
import com.rookia.gotflights.utils.DateUtils

interface Leg {
    val airline: String?
    val airlineImage: String?
    val arrivalDate: String?
    val arrivalTime: String?
    val departureDate: String?
    val departureTime: String?
    val destination: String?
    val origin: String?

    fun getElapsedTime(resourcesManager: ResourcesManager): String {
        val departure = DateUtils.parseStringDate(departureDate, departureTime) ?: return ""
        val arrival = DateUtils.parseStringDate(arrivalDate, arrivalTime) ?: return ""
        return DateUtils.elapsedTime(departure, arrival, resourcesManager)
    }
}