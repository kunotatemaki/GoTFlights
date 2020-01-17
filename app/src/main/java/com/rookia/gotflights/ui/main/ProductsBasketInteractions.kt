package com.rookia.gotflights.ui.main


interface ProductsBasketInteractions {
    fun addProductToBasket(code: String?)
    fun removeProductToBasket(code: String?)
    fun clearBasket()
}