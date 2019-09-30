package com.gt.go4lunch.data.repositories.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OpeningHoursDao {

    @Query("SELECT * FROM OpeningHoursTable WHERE opening_id=:openingHoursID")
    suspend fun getOpeningHours(openingHoursID: Int): OpeningHoursTable

    @Insert
    suspend fun insertOpeningHours(openingHours: OpeningHoursTable)

    @Query("DELETE FROM OpeningHoursTable")
    suspend fun deleteAllOpeningHours()
}