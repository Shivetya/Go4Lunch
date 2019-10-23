package com.gt.go4lunch.data.repositories.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OpenTable(@PrimaryKey (autoGenerate = true) @ColumnInfo(name = "open_id") val openId: Int = 0,
                     val dayopen: Int?,
                     val timeopen: String?)