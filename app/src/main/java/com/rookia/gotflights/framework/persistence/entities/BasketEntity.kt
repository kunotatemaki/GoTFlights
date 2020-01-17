package com.rookia.gotflights.framework.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "basket", indices = [(Index(value = arrayOf("code"), unique = true))])
data class BasketEntity constructor(
    @PrimaryKey
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "selections")
    var selections: Int
)

