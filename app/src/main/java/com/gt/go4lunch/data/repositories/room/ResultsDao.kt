package com.gt.go4lunch.data.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResultsDao {

    @Query("SELECT * FROM resultTable")
    suspend fun getResultsByCache(): List<ResultTable>

    @Insert
    suspend fun insertResult(result: ResultTable)

    @Query("DELETE FROM ResultTable")
    suspend fun deleteAllResults()
}