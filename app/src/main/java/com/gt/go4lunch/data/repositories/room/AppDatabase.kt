package com.gt.go4lunch.data.repositories.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gt.go4lunch.MainApplication

@Database(entities = [ResultTable::class, OpeningHoursTable::class, LocationTable::class, GeometryTable::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun resultDao(): ResultsDao
    abstract fun openingHoursDao(): OpeningHoursDao
    abstract fun locationDao(): LocationDao
    abstract fun geometryDao(): GeometryDao

    companion object {
        private lateinit var INSTANCE: AppDatabase

        fun getInstance(): AppDatabase {
            if (!::INSTANCE.isInitialized) {
                synchronized(AppDatabase::class.java) {
                    if (!::INSTANCE.isInitialized) {
                        INSTANCE = Room.databaseBuilder(
                            MainApplication.getInstance(),
                            AppDatabase::class.java, "Database.db"
                        ).build()
                    }
                }
            }

            return INSTANCE
        }
    }
}