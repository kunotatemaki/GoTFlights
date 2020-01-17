package com.rookia.gotflights.domain.network.model

interface LegApiResponse {
    val airline: String?
    val airlineImage: String?
    val arrivalDate: String?
    val arrivalTime: String?
    val departureDate: String?
    val departureTime: String?
    val destination: String?
    val origin: String?
}