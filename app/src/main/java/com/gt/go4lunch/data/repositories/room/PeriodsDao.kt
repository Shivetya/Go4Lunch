package com.gt.go4lunch.data.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PeriodsDao {

    @Query("""
        SELECT *
        FROM PeriodsTable
        WHERE restaurant_id = :restaurantID
            AND :dateTimeNow < ttl 
    """)
    suspend fun getPeriodsByCache(restaurantID: String, dateTimeNow: String): List<PeriodsTable>

    @Insert
    suspend fun insertPeriod(period: PeriodsTable)

    @Query("DELETE FROM PeriodsTable where :dateTimeNow > ttl")
    suspend fun deleteAllPeriods(dateTimeNow: String)
}