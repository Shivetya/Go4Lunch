package com.gt.go4lunch.data.repositories.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OpeningHoursTable(@PrimaryKey (autoGenerate = true) @ColumnInfo(name = "opening_id") val openingHoursID: Int = 0,
                             @ColumnInfo(name = "open_now") val openNow: Boolean?)