package com.rookia.gotflights.domain.model

import java.math.BigDecimal

data class Flight(
    val currency: String?,
    val inbound: Inbound?,
    val outbound: Outbound?,
    val price: BigDecimal?
)