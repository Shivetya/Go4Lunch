package com.gt.go4lunch.data.repositories.room

import androidx.room.*

@Entity
data class OpeningHoursTable(@PrimaryKey (autoGenerate = true) @ColumnInfo(name = "opening_id") val openingHoursID: Int = 0,
                             @ColumnInfo(name = "open_now") val openNow: Boolean? = null,
                             @Relation(parentColumn = "id", entityColumn = "opening_hours_id", entity = PeriodsTable::class)
                             val periods: List<PeriodsTable?>?)