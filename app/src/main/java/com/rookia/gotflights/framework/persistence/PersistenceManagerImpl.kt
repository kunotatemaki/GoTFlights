package com.rookia.gotflights.framework.persistence

import androidx.lifecycle.LiveData
import com.rookia.gotflights.domain.model.mapper.ApiToEntityMapper
import com.rookia.gotflights.framework.persistence.databases.AppDatabase
import com.rookia.gotflights.framework.persistence.entities.BasketEntity
import com.rookia.gotflights.framework.persistence.entities.ProductEntity
import com.rookia.gotflights.domain.network.AppApi.Item
import com.rookia.gotflights.data.persistence.PersistenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersistenceManagerImpl @Inject constructor(
    private val db: AppDatabase
) : PersistenceManager {

    override fun getProducts(): LiveData<List<ProductEntity>> =
        db.productDao().getProducts()

    override suspend fun storeProducts(products: List<Item>) {
        db.productDao().insert(products.map { ApiToEntityMapper.map(it) })
    }

    override fun getProductsInBasket(): LiveData<List<BasketEntity>> =
        db.basketDao().getProductsInBasket()

    override suspend fun getProductFromBasket(code: String): BasketEntity? =
        db.basketDao().getProductFromBasket(code)

    override suspend fun addProductToBasket(code: String) {
        db.basketDao().insertAndDeleteInTransaction(code, 1)
    }

    override suspend fun removeProductFromBasket(code: String) {
        db.basketDao().insertAndDeleteInTransaction(code, -1)
    }

    override suspend fun clearBasket() {
        db.basketDao().delete()
    }
}

