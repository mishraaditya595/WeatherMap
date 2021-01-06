package com.vob.weathermap.db

import androidx.lifecycle.LiveData

class WeatherRepository(private val weatherDAO: WeatherDAO) {

    val readAllData: LiveData<List<WeatherDbModel>> = weatherDAO.readAllData()

    suspend fun addData(data: WeatherDbModel) {
        weatherDAO.addWeatherData(data)
    }
}