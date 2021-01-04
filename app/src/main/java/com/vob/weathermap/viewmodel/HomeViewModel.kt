package com.vob.weathermap.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vob.weathermap.model.LocationLiveData
import com.vob.weathermap.model.WeatherModel
import com.vob.weathermap.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel(context: Context, private val repository: Repository): ViewModel() {

    private val locationData = LocationLiveData(context)
    val response = MutableLiveData<WeatherModel>()

    fun getLocationData() = locationData

    fun getWeatherData(lat: String, lon: String, appid: String) {
        viewModelScope.launch {
            val getResponse = repository.getWeatherData(lat, lon, appid)
            response.value = getResponse
        }
    }
}