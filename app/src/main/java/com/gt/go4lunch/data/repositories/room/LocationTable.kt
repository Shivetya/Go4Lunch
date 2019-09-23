package com.gt.go4lunch.data.repositories.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class LocationTable (@PrimaryKey val locationID: Int,
                     val lat: Double,
                     val lng: Double)