package com.gt.go4lunch.data.repositories.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CloseTable (@PrimaryKey (autoGenerate = true) @ColumnInfo (name = "close_id") val closeID: Int = 0,
                       val day: Int?,
                       val time: String?)