package com.example.udacityasteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.example.udacityasteroidradar.Constants
import com.example.udacityasteroidradar.api.AsteroidsApi
import com.example.udacityasteroidradar.api.getNextSevenDaysFormattedDates
import com.example.udacityasteroidradar.api.parseAsteroidsJsonResult
import com.example.udacityasteroidradar.database.AsteroidsDatabase
import com.example.udacityasteroidradar.database.asDomainModel
import com.example.udacityasteroidradar.models.Asteroid
import com.example.udacityasteroidradar.models.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val asteroidsDatabase: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(asteroidsDatabase.asteroidDao.getWeekAsteroids().asLiveData()){
        Log.i("Repository",it.size.toString())
        it.asDomainModel()
    }

    suspend fun refreshData() {
        val nextSevenDaysList = getNextSevenDaysFormattedDates()
        val parametersMap = mapOf(
            "start_date" to nextSevenDaysList[0],
            "end_date" to nextSevenDaysList.last(),
            "api_key" to Constants.API_KEY
        )
        withContext(Dispatchers.IO) {
            val networkAsteroids = AsteroidsApi.retrofitService.getAsteroids(parametersMap)
            val asteroidsList = parseAsteroidsJsonResult(JSONObject(networkAsteroids))
            asteroidsDatabase.asteroidDao.insertAll(asteroidsList.asDatabaseModel())
        }
    }
}