package com.rookia.gotflights.framework.persistence

import androidx.lifecycle.LiveData
import com.rookia.gotflights.framework.persistence.databases.AppDatabase
import com.rookia.gotflights.data.persistence.PersistenceManager
import com.rookia.gotflights.framework.persistence.entities.FooEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersistenceManagerImpl @Inject constructor(
    private val db: AppDatabase
) : PersistenceManager {

    override fun getFoo(): LiveData<List<FooEntity>> =
        db.fooDao().getFoo()

    override suspend fun storeFoo(list: List<FooEntity>) {
        db.fooDao().insert(list)
    }
}

