package com.vob.weathermap.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vob.weathermap.db.WeatherDB
import com.vob.weathermap.db.WeatherDbModel
import com.vob.weathermap.db.WeatherRepository
import com.vob.weathermap.model.LocationLiveData
import com.vob.weathermap.model.WeatherModel
import com.vob.weathermap.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class HomeViewModel(context: Context, private val repository: Repository): ViewModel() {

    private val locationData = LocationLiveData(context)
    val response = MutableLiveData<WeatherModel>()

    private val readOfflineData: LiveData<List<WeatherDbModel>>
    private val weatherDB_repo: WeatherRepository

    init {
        val weatherDAO = WeatherDB.getDatabase(context).weatherDao()
        weatherDB_repo = WeatherRepository(weatherDAO)
        readOfflineData = weatherDB_repo.readAllData
    }

    fun getLocationData() = locationData

    fun getWeatherData(lat: String, lon: String, appid: String) {
        viewModelScope.launch {
            val getResponse = repository.getWeatherData(lat, lon, appid)
            response.value = getResponse
        }
    }

    fun addWeatherData(data: WeatherDbModel) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherDB_repo.addData(data)
        }
    }

}