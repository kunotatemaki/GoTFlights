package com.rookia.gotflights.domain.network.model

data class OutboundApiResponse(
    override val airline: String?,
    override val airlineImage: String?,
    override val arrivalDate: String?,
    override val arrivalTime: String?,
    override val departureDate: String?,
    override val departureTime: String?,
    override val destination: String?,
    override val origin: String?
) : LegApiResponse