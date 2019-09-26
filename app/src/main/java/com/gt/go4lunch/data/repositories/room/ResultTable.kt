package com.gt.go4lunch.data.repositories.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey

import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class ResultTable(
    @PrimaryKey val resultID: Int,
    @Embedded val geometry: GeometryTable,
    @Embedded val openingHours: OpeningHoursTable?,
    val types: String?,
    val name: String,
    val iconURL: String,
    val vicinity: String,
    val ttl: LocalDateTime
)