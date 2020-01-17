package com.rookia.gotflights.framework.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "foo", indices = [(Index(value = arrayOf("id"), unique = true))])
data class FooEntity constructor(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "price")
    var price: Double
)

