package com.gt.go4lunch.data.repositories.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationTable (@PrimaryKey (autoGenerate = true) @ColumnInfo(name = "location_id") val locationID: Int = 0,
                          val lat: Double,
                          val lng: Double)