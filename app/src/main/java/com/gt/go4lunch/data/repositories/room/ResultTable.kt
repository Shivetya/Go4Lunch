package com.gt.go4lunch.data.repositories.room

import androidx.room.Entity
import androidx.room.ForeignKey

import androidx.room.PrimaryKey

@Entity( foreignKeys = [ForeignKey(
    entity =GeometryTable::class,
    parentColumns = ["resultID"],
    childColumns = ["geometryID"],
    onDelete = ForeignKey.CASCADE
    ), ForeignKey(
    entity = OpeningHoursTable::class,
    parentColumns = ["resultID"],
    childColumns = ["openingHoursID"],
    onDelete = ForeignKey.CASCADE
    )]
)
data class ResultTable (@PrimaryKey val resultID: Int,
                        val geometryID: Int,
                        val openingHoursID: Int?,
                        val types: String?,
                        val name: String,
                        val iconURL: String,
                        val vicinity: String)