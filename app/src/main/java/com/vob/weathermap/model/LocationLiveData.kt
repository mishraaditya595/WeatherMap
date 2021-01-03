package com.vob.weathermap.model

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationLiveData(context: Context): LiveData<LocationModel>() {

    private var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    companion object{
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationResult?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun onActive(){
        super.onActive()
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            it?.also { location ->
                setLocationData(location)
            }
        }
        startLocationUpdates()
    }

    private fun setLocationData(response: Location) {
        value = LocationModel(
            longitude = response.longitude.toString(),
            latitude = response.latitude.toString(),
            time = response.time.toString()
        )
    }
}