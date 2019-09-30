package com.gt.go4lunch.data.repositories.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeometryTable(@PrimaryKey (autoGenerate = true) @ColumnInfo(name = "geometry_id") val geometryID: Int = 0,
                         @Embedded val location: LocationTable
)