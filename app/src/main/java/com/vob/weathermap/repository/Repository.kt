package com.vob.weathermap.repository

import com.vob.weathermap.model.RetrofitInstance
import com.vob.weathermap.model.WeatherModel
import retrofit2.Call

class Repository {
    suspend fun getWeatherData(lat: String, lon: String, appid: String): WeatherModel {
        return RetrofitInstance().api.getWeatherData(lat, lon, appid)
    }
}