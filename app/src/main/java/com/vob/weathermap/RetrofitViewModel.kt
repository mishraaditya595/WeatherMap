package com.vob.weathermap

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vob.weathermap.model.WeatherModel
import com.vob.weathermap.repository.Repository
import kotlinx.coroutines.launch

class RetrofitViewModel(private val repository: Repository): ViewModel() {

    val response: MutableLiveData<WeatherModel> = MutableLiveData()

    fun getWeatherData(lat: String, lon: String, appid: String) {
        viewModelScope.launch {
            val getResponse = repository.getWeatherData(lat, lon, appid)
            response.value = getResponse
        }
    }
}