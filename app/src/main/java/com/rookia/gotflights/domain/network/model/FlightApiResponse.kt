package com.rookia.gotflights.domain.network.model

import java.math.BigDecimal

data class FlightApiResponse(
    val currency: String?,
    val inbound: InboundApiResponse?,
    val outbound: OutboundApiResponse?,
    val price: BigDecimal?
)