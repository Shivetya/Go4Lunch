package com.gt.go4lunch.data.repositories.room

import androidx.room.*

@Entity
data class PeriodsTable(@PrimaryKey (autoGenerate = true) @ColumnInfo (name = "period_id") val periodsID: Int = 0,
                        @Embedded val close: CloseTable?,
                        @Embedded val open: OpenTable?,
                        @ColumnInfo(name = "restaurant_id") val restaurantId: String,
                        val ttl: String)