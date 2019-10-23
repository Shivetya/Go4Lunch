package com.gt.go4lunch.data.repositories.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ResultDetailsTable(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val ttl: String,
    @ColumnInfo(name = "restaurant_id") val restaurantId: String,
    val rating: Double?
    )