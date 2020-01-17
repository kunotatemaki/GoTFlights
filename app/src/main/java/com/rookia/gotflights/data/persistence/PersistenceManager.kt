package com.rookia.gotflights.data.persistence

import androidx.lifecycle.LiveData
import com.rookia.gotflights.framework.persistence.entities.FooEntity

interface PersistenceManager {

    fun getFoo(): LiveData<List<FooEntity>>
    suspend fun storeFoo(list: List<FooEntity>)

}