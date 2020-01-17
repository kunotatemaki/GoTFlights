package com.rookia.gotflights.framework.persistence.utils

import androidx.room.TypeConverter
import java.util.*

@Suppress("unused")
class DbConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}