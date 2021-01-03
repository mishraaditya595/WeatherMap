package com.vob.weathermap

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.vob.weathermap.databinding.ActivityRetrofitBinding
import com.vob.weathermap.model.WeatherApi
import com.vob.weathermap.model.WeatherModel
import com.vob.weathermap.repository.Repository
import com.vob.weathermap.util.Constants
import com.vob.weathermap.util.Constants.Companion.API_ID
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class RetrofitActivity : AppCompatActivity() {

    lateinit var binding: ActivityRetrofitBinding
    private lateinit var viewModel: RetrofitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetrofitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Repository()
        val viewModelFactory = RetrofitViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RetrofitViewModel::class.java)
        viewModel.getWeatherData("28.7041", "77.1025", API_ID)

        viewModel.response.observe(this, androidx.lifecycle.Observer { response ->
            binding.data.text = response.main.temp.toString()
        })
    }

//        val api = Retrofit.Builder()
//            .baseUrl("https://api.openweathermap.org/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(WeatherApi::class.java)
//
//        var latitude: String
//        var longitude: String
//
//        //getLocation()
//
//        GlobalScope.launch(Dispatchers.IO) {
//            delay(5000L)
//            val call = api.getWeatherData("28.7041", "77.1025", Constants.API_ID).awaitResponse()
//            if (call.isSuccessful) {
//                val temp = call.body()!!.main.temp
//                withContext(Dispatchers.Main){
//                    binding.data.text = temp.toBigDecimal().toPlainString()
//                    binding.date.text = getDate(call.body()!!.dt)
//                }
//            }
//            else
//            {
//                withContext(Dispatchers.Main){
//                    binding.data.text = "Failed"
//                }
//            }
//        }
//    }
//    @SuppressLint("MissingPermission")
//    fun getLocation() {
//        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//
//        val lat = locationGPS!!.latitude
//        val longi = locationGPS.longitude
//        val latitude = lat.toBigDecimal().toPlainString()
//        val longitude = longi.toBigDecimal().toPlainString()
//    }
//
//    fun getDate(i: Int): String {
//
//        val date = Date(i.toLong())
//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")
//        sdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
//        val timeFormat = sdf.format(date)
//        return timeFormat
//    }
}