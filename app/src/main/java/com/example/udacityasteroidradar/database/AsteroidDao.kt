package com.example.udacityasteroidradar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM AsteroidsEntity ORDER BY close_approach_data ASC")
    fun getWeekAsteroids(): Flow<List<AsteroidsEntity>>

    @Query("SELECT * FROM AsteroidsEntity WHERE close_approach_data = :today ORDER BY close_approach_data ASC")
    fun getTodayAsteroids(today: String): Flow<List<AsteroidsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: Array<AsteroidsEntity>)

    @Query("DELETE FROM AsteroidsEntity WHERE close_approach_data < :today")
    suspend fun deleteOldAsteroids(today: String)
}