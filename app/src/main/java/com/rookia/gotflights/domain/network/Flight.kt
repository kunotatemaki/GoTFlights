package com.rookia.gotflights.domain.network

import java.math.BigDecimal

data class Flight(
    val currency: String?,
    val inbound: Inbound?,
    val outbound: Outbound?,
    val price: BigDecimal?
)