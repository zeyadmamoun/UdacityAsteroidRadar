package com.example.udacityasteroidradar.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PictureOfDay( @Json(name = "media_type") val mediaType: String,
                         val title: String,
                         val url: String)