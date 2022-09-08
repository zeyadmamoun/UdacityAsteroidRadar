package com.example.udacityasteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidsEntity::class], version = 1, exportSchema = false)
abstract class AsteroidsDatabase : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    companion object{
        @Volatile
        var INSTANCE: AsteroidsDatabase? = null

        fun getDatabase(context: Context): AsteroidsDatabase {
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidsDatabase::class.java,
                    "Asteroid_Database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}