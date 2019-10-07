package com.gt.go4lunch.data.repositories.room

import androidx.room.*

@Entity
data class ResultTable(
    @PrimaryKey (autoGenerate = true) @ColumnInfo(name = "result_id") val resultID: Int = 0,
    @Embedded val geometry: GeometryTable? = null,
    @Embedded val opening_hours: OpeningHoursTable?,
    val types: String? = null,
    val name: String? = null,
    val vicinity: String? = null,
    @ColumnInfo(name = "restaurant_id") val restaurantId: String?,
    val rating: Double? = 0.0,
    val ttl: String,
    @ColumnInfo(name = "lat_user") val latUser: Double? = null,
    @ColumnInfo(name = "lng_user") val lngUser: Double? = null
)