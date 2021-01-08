package com.vob.weathermap.model

import retrofit2.http.Query
import retrofit2.http.GET

interface AirQualityApi {

    @GET("data/2.5/air_pollution?")
    suspend fun getAirQualityData(
            @Query("lat") lat: String,
            @Query("lon") lon: String,
            @Query("appid") appid: String
    ):AqiModel
}