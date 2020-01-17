package com.rookia.gotflights.framework.persistence.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rookia.gotflights.framework.persistence.daos.FooDao
import com.rookia.gotflights.framework.persistence.entities.FooEntity
import com.rookia.gotflights.framework.persistence.utils.DbConverters
import com.rookia.gotflights.framework.persistence.utils.PersistenceConstants

@Database(entities = [(FooEntity::class)], exportSchema = false, version = 1)
@TypeConverters(DbConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fooDao(): FooDao

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
