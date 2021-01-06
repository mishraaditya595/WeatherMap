package com.vob.weathermap.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data")
data class WeatherDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val time: Long,
    val temp: Double,
    val wind: Double,
    val pressure: Int,
    val lat: String,
    val lon: String,
    val visibility: Int,
    val humidity: Int
)