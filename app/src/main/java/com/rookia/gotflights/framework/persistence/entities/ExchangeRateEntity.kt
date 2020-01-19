package com.rookia.gotflights.framework.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "exchange_rate",
    indices = [(Index(value = arrayOf("relation"), unique = true))]
)
data class ExchangeRateEntity constructor(
    @PrimaryKey
    @ColumnInfo(name = "relation")
    val relation: String,
    @ColumnInfo(name = "from")
    val from: String?,
    @ColumnInfo(name = "to")
    val to: String?,
    @ColumnInfo(name = "value")
    var value: Double?,
    @ColumnInfo(name = "date")
    var date: Date
)

