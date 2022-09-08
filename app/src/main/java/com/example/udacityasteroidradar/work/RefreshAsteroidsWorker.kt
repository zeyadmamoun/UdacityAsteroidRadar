package com.example.udacityasteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.udacityasteroidradar.api.getNextSevenDaysFormattedDates
import com.example.udacityasteroidradar.database.AsteroidsDatabase.Companion.getDatabase
import com.example.udacityasteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshAsteroidsWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext,workerParams){

    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)

        return try {
            repository.refreshData()
            database.asteroidDao.deleteOldAsteroids(getNextSevenDaysFormattedDates()[0])
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}