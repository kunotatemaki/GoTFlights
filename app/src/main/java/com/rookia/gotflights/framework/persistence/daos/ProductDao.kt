package com.rookia.gotflights.framework.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.rookia.gotflights.framework.persistence.entities.ProductEntity


@Dao
abstract class ProductDao : BaseDao<ProductEntity>() {

    @Query("SELECT * FROM product")
    abstract fun getProducts(): LiveData<List<ProductEntity>>

}