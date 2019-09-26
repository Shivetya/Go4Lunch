package com.gt.go4lunch.data.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {

    @Query("SELECT * FROM LocationTable WHERE locationID == :locationID")
    suspend fun getLocation(locationID: Int): LocationTable

    @Insert
    suspend fun insertLocation(location: LocationTable)

    @Query("DELETE FROM LocationTable")
    suspend fun deleteAllLocations()
}