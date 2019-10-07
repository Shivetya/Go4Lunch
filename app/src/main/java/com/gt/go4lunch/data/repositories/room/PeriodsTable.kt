package com.gt.go4lunch.data.repositories.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PeriodsTable(@PrimaryKey (autoGenerate = true) @ColumnInfo (name = "periods_id") val periodsID: Int = 0,
                        @Embedded val close: CloseTable?,
                        @Embedded val open: OpenTable?)