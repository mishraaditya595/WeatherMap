package com.vob.weathermap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.vob.weathermap.databinding.ActivityMainBinding
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.vob.weathermap.util.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var latitude: String
    lateinit var longitude: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkForLocationPermission()
        getLocation()
        GlobalScope.launch {
            callApi()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                latitude = location.latitude.toString()
                longitude = location.longitude.toString()
            }
        }

        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

    }

    private fun checkForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            val arrayOfPermission = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION
                    ,Manifest.permission.ACCESS_COARSE_LOCATION)

            ActivityCompat.requestPermissions(this, arrayOfPermission, 8090)
        }
    }

    suspend fun callApi() {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
                .url("${Constants.WEATHER_URL}lat=$latitude&lon=$longitude&appid=${Constants.API_ID}")
                .get()
                .build()

        try {
            val response = okHttpClient.newCall(request).execute()
            val data = response.body().string()

        } catch (e: Exception) {
        }
    }

}