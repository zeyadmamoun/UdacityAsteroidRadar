package com.example.udacityasteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.udacityasteroidradar.api.AsteroidsApi
import com.example.udacityasteroidradar.api.getNextSevenDaysFormattedDates
import com.example.udacityasteroidradar.database.AsteroidsDatabase
import com.example.udacityasteroidradar.database.asDomainModel
import com.example.udacityasteroidradar.models.Asteroid
import com.example.udacityasteroidradar.models.PictureOfDay
import com.example.udacityasteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidsDatabase.getDatabase(application)
    private val repository = AsteroidsRepository(database)

    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids : LiveData<List<Asteroid>>
        get() = _asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay


    init {
        _asteroids = repository.asteroids as MutableLiveData<List<Asteroid>>
        getPictureOfTheDay()
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                repository.refreshData()
            } catch (e: IOException) {
                e.message?.let { Log.i("MainViewModel", it) }
            }
        }
    }

    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = AsteroidsApi.retrofitService.getImageOfTheDay()
            } catch (e: IOException) {
                e.let { Log.i("MainViewModel", it.toString()) }
            }
        }
    }

    suspend fun onWeekSelected() {
        database.asteroidDao.getWeekAsteroids().collect{
            _asteroids.value = it.asDomainModel()
        }
    }

    suspend fun onTodaySelected() {
        val today = getNextSevenDaysFormattedDates()[0]
        database.asteroidDao.getTodayAsteroids(today).collect{
            _asteroids.value = it.asDomainModel()
        }
    }
}

class MainViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}