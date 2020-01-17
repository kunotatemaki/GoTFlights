package com.rookia.gotflights.domain.network.model

import java.math.BigDecimal

data class ExchangeRate(
    val currency: String?,
    val exchangeRate: BigDecimal?
)