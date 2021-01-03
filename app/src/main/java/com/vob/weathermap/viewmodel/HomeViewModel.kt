package com.vob.weathermap.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.vob.weathermap.model.LocationLiveData

class HomeViewModel(context: Context): ViewModel() {

    private val locationData = LocationLiveData(context)

    fun getLocationData() = locationData

//    @SuppressLint("MissingPermission")
//    fun currentLocation() {
//        val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
//        val job = viewModelScope.launch {
//            mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
//                location.value = LocationModel(it.latitude.toString(), it.longitude.toString(), it.time.toString())
//            }
//        }
//    }
}