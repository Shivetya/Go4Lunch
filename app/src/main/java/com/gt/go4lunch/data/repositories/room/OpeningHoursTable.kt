package com.gt.go4lunch.data.repositories.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OpeningHoursTable(@PrimaryKey val openingHoursID: Int,
                             val openNow: Boolean)