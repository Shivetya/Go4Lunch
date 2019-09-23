package com.gt.go4lunch.data.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimeLastRequestDao {

    @Query("SELECT * FROM TimeLastRequestTable")
    suspend fun getLastTimeRequest(): TimeLastRequestTable?

    @Insert
    suspend fun insertTimeRequest()

    @Query("DELETE FROM TimeLastRequestTable")
    suspend fun deleteAllTimeRequests()
}