package com.rookia.gotflights.framework.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.rookia.gotflights.framework.persistence.entities.FooEntity


@Dao
abstract class FooDao : BaseDao<FooEntity>() {

    @Query("SELECT * FROM foo")
    abstract fun getFoo(): LiveData<List<FooEntity>>

}