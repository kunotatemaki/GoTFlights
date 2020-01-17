package com.rookia.gotflights.domain.model

interface Leg {
    val airline: String?
    val airlineImage: String?
    val arrivalDate: String?
    val arrivalTime: String?
    val departureDate: String?
    val departureTime: String?
    val destination: String?
    val origin: String?
    val elapsedTime: String?
}