package com.rookia.gotflights.domain

import com.rookia.gotflights.data.resources.ResourcesManager
import com.rookia.gotflights.domain.network.model.LegApiResponse
import com.rookia.gotflights.utils.DateUtils


fun getElapsedTime(leg: LegApiResponse, resourcesManager: ResourcesManager): String {
    val departure = DateUtils.parseStringDate(leg.departureDate, leg.departureTime)
    val arrival = DateUtils.parseStringDate(leg.arrivalDate, leg.arrivalTime)
    return ""
}