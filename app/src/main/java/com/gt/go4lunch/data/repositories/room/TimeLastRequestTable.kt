package com.gt.go4lunch.data.repositories.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimeLastRequestTable (@PrimaryKey val timeID: Int = 0,
                                 val time: String)