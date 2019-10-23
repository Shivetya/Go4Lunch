package com.gt.go4lunch.data.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResultsDao {

    //regarder tuple room

    @Query("""SELECT * 
        FROM resultTable
        where :dateTimeNow < ttl 
            AND (lat_user BETWEEN :latUser - 0.05 AND :latUser + 0.005) 
            AND (lng_user BETWEEN :lngUser - 0.005 AND :lngUser + 0.005)""")
    suspend fun getResultsByCache(latUser: Double, lngUser: Double, dateTimeNow: String): List<ResultTable>

    @Insert
    suspend fun insertResult(result: ResultTable)

    @Query("DELETE FROM ResultTable where :dateTimeNow > ttl")
    suspend fun deleteAllResults(dateTimeNow: String)


}