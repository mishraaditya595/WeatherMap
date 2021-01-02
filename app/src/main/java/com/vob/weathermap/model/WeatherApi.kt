package com.vob.weathermap.model

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather?")
    fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("APPID") appid: String
    ): Call<WeatherModel>

}