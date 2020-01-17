package com.rookia.gotflights.domain.model

data class Product (
    val code: String,
    val name: String,
    var price: Double,
    var timesInBasket: Int = 0, // used for calculation of discounts
    var allowOffersInItem: Boolean = true //used for non accumulative offers (if required)
)
