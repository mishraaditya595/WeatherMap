package com.vob.weathermap

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vob.weathermap.databinding.ActivityRetrofitBinding
import com.vob.weathermap.model.WeatherApi
import com.vob.weathermap.model.WeatherModel
import com.vob.weathermap.util.Constants
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitActivity : AppCompatActivity() {

    lateinit var binding: ActivityRetrofitBinding
    //lateinit var latitude: String
    //ateinit var longitude: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetrofitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val api = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)

        var latitude: String
        var longitude: String

        //getLocation()

        GlobalScope.launch(Dispatchers.IO) {
            delay(5000L)
            val call = api.getWeatherData("28.7041", "77.1025", Constants.API_ID).main
//            if (call.isSuccessful) {
//                val temp = call.body().toString()
//                withContext(Dispatchers.Main){
//                    binding.data.text = temp
//                }
//            }
//            else
//            {
//                withContext(Dispatchers.Main){
//                    binding.data.text = "Failed"
//                }
//            }
            withContext(Dispatchers.Main){
                binding.data.text = call.temp.toString()
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun getLocation() {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        val lat = locationGPS!!.latitude
        val longi = locationGPS.longitude
        val latitude = lat.toBigDecimal().toPlainString()
        val longitude = longi.toBigDecimal().toPlainString()
    }
}