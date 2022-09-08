package com.example.udacityasteroidradar.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.udacityasteroidradar.main.MainViewModel
import com.example.udacityasteroidradar.models.Asteroid

class DetailViewModel(asteroid: Asteroid) : ViewModel() {
    init {
        Log.i("MainViewModel",asteroid.toString())
    }
}

class DetailViewModelFactory(private val asteroid: Asteroid) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(asteroid) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}