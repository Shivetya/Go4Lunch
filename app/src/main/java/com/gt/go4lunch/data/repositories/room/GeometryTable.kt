package com.gt.go4lunch.data.repositories.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeometryTable(@PrimaryKey val geometryID: Int,
                         val locationID: Int
)