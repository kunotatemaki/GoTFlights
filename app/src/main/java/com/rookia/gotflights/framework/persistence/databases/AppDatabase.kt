package com.rookia.gotflights.framework.persistence.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rookia.gotflights.framework.persistence.daos.BasketDao
import com.rookia.gotflights.framework.persistence.daos.ProductDao
import com.rookia.gotflights.framework.persistence.entities.BasketEntity
import com.rookia.gotflights.framework.persistence.entities.ProductEntity
import com.rookia.gotflights.framework.persistence.utils.DbConverters
import com.rookia.gotflights.framework.persistence.utils.PersistenceConstants

@Database(entities = [(ProductEntity::class), (BasketEntity::class)], exportSchema = false, version = 1)
@TypeConverters(DbConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun basketDao(): BasketDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: buildDatabase(
                context
            ).also { INSTANCE = it }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java, PersistenceConstants.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()

    }
}
