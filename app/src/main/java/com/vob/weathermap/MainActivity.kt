package com.vob.weathermap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.JsonReader
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.vob.weathermap.databinding.ActivityMainBinding
import com.vob.weathermap.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.System.`in`

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    companion object{
        lateinit var latitude: String
        lateinit var longitude: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkForLocationPermission()

        getLocation()


        binding.text.text = "Lat: $latitude Longi: $longitude"

        var data: String = "xyz"

        GlobalScope.launch {

            data = getWeatherData()

            val gson = Gson()
            val model = gson.fromJson<WeatherModel>(data, WeatherModel::class.java)
            withContext(Dispatchers.Main) {
                binding.text.text = data
                Thread.sleep(2000L)
                binding.text.text = model.main.temp.toBigDecimal().toPlainString()
            }
        }

    }

    private fun getWeatherData(): String {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
                .url("${Constants.WEATHER_URL}lat=$latitude&lon=$longitude&appid=${Constants.API_ID}")
                .get()
                .build()

        val response = okHttpClient.newCall(request).execute()
        val data = response.body().string()
        return data
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        val lat = locationGPS!!.latitude
        val longi = locationGPS.longitude
        latitude = lat.toBigDecimal().toPlainString()
        longitude = longi.toBigDecimal().toPlainString()
    }


    private fun checkForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            val arrayOfPermission = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

            ActivityCompat.requestPermissions(this, arrayOfPermission, 8090)
        }
    }


}