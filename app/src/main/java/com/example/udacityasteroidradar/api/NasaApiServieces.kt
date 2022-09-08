package com.example.udacityasteroidradar.api

import com.example.udacityasteroidradar.Constants
import com.example.udacityasteroidradar.Constants.BASE_URL
import com.example.udacityasteroidradar.models.PictureOfDay
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NasaApiServices {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@QueryMap params: Map<String, String>): String

    @GET("planetary/apod")
    suspend fun getImageOfTheDay(@Query("api_key") apiKey: String = Constants.API_KEY): PictureOfDay
}

object AsteroidsApi {
    val retrofitService: NasaApiServices by lazy {
        retrofit.create(NasaApiServices::class.java)
    }
}