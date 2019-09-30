package com.gt.go4lunch.data.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GeometryDao {

    @Query("SELECT * FROM GeometryTable WHERE geometry_id=:geometryID")
    suspend fun getGeometry(geometryID: Int): GeometryTable

    @Insert
    suspend fun insertGeometry(geometry: GeometryTable)

    @Query("DELETE FROM GeometryTable")
    suspend fun deleteAllGeometry()
}