package com.vob.weathermap.repository

import com.vob.weathermap.model.AqiModel
import com.vob.weathermap.model.RetrofitInstance
import com.vob.weathermap.model.WeatherModel

class Repository {
    suspend fun getWeatherData(lat: String, lon: String, appid: String): WeatherModel {
        return RetrofitInstance().weatherApi.getWeatherData(lat, lon, appid)
    }

    suspend fun getAirQualityData(lat: String, lon: String, appid: String): AqiModel {
        return RetrofitInstance().airQualityApi.getAirQualityData(lat, lon, appid)
    }
}