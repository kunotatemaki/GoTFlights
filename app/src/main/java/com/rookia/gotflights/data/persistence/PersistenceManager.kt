package com.rookia.gotflights.data.persistence

import androidx.lifecycle.LiveData
import com.rookia.gotflights.domain.network.AppApi.Item
import com.rookia.gotflights.framework.persistence.entities.BasketEntity
import com.rookia.gotflights.framework.persistence.entities.ProductEntity

interface PersistenceManager {

    fun getProducts(): LiveData<List<ProductEntity>>
    suspend fun storeProducts(products: List<Item>)
    fun getProductsInBasket(): LiveData<List<BasketEntity>>
    suspend fun getProductFromBasket(code: String): BasketEntity?
    suspend fun addProductToBasket(code: String)
    suspend fun removeProductFromBasket(code: String)
    suspend fun clearBasket()

}