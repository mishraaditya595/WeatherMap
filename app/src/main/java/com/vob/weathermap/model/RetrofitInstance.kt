package com.vob.weathermap.model

import com.vob.weathermap.util.Constants.Companion.WEATHER_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    private val retrofit by lazy {

        Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val weatherApi: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }

    val airQualityApi: AirQualityApi by lazy {
        retrofit.create(AirQualityApi::class.java)
    }

}