package com.rookia.gotflights.domain.network

interface Leg {
    val airline: String?
    val airlineImage: String?
    val arrivalDate: String?
    val arrivalTime: String?
    val departureDate: String?
    val departureTime: String?
    val destination: String?
    val origin: String?
}