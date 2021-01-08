package com.vob.weathermap.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vob.weathermap.model.WeatherModel

@Entity(tableName = "weather_data")
data class WeatherDbModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val cityName: String,
    val time: Long,
    val temp: Double,
    val wind: Double,
    val pressure: Int,
    val lat: String,
    val lon: String,
    val visibility: Int,
    val humidity: Int,
    val clouds: Int,
    val dewPoints: Double,
    val uvi: Double,
    val feelsLike: Double
)