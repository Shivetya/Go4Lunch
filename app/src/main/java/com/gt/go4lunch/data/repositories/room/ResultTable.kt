package com.gt.go4lunch.data.repositories.room

import androidx.room.*

@Entity
data class ResultTable(
    @PrimaryKey (autoGenerate = true) @ColumnInfo(name = "result_id") val resultID: Int = 0,
    @Embedded val geometry: GeometryTable,
    @Embedded val opening_hours: OpeningHoursTable?,
    val types: String?,
    val name: String,
    @ColumnInfo(name = "icon_url") val iconURL : String?,
    val vicinity: String,
    val ttl: String,
    @ColumnInfo(name = "lat_user") val latUser: Double,
    @ColumnInfo(name = "lng_user") val lngUser: Double
)