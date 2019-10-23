package com.gt.go4lunch.data.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResultDetailsDao {

    @Query("""
        SELECT rating
        FROM ResultDetailsTable
        WHERE restaurant_id = :restaurantID
            AND :dateTimeNow < ttl 
    """)
    suspend fun getRatingByCache(restaurantID: String, dateTimeNow: String): Double?

    @Insert
    suspend fun insertResultDetails(resultDetails: ResultDetailsTable)

    @Query("DELETE FROM ResultDetailsTable where :dateTimeNow > ttl")
    suspend fun deleteAllResultDetails(dateTimeNow: String)
}