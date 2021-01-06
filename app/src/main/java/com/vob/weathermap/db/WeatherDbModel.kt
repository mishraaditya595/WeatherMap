package com.vob.weathermap.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data")
data class WeatherDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val time: String,
    val temp: String,
    val wind: String,
    val pressure: String,
    val lat: String,
    val lon: String,
    val visibility: String,
    val humidity: String
)