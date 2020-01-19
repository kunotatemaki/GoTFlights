package com.rookia.gotflights.domain.network.model


data class FlightApiResponse(
    val currency: String?,
    val inbound: InboundApiResponse?,
    val outbound: OutboundApiResponse?,
    val price: Double?
)