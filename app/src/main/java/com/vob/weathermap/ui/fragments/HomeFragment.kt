package com.vob.weathermap.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vob.weathermap.R
import com.vob.weathermap.WeatherApplication
import com.vob.weathermap.databinding.FragmentHomeBinding
import com.vob.weathermap.db.WeatherDbModel
import com.vob.weathermap.repository.Repository
import com.vob.weathermap.util.Constants.Companion.API_ID
import com.vob.weathermap.viewmodel.HomeViewModel
import com.vob.weathermap.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

@InternalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var latitude: String
    private lateinit var longitude: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val repository = Repository()
        val viewModelFactory = HomeViewModelFactory(requireContext(), repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        latitude = ""
        longitude = ""

//        viewModel.getLocationData().observe(viewLifecycleOwner, Observer {
//            binding.cordinates.text = "Lat:${it.latitude}  Lon:${it.longitude}"
//            latitude = it.latitude.toString()
//            longitude = it.longitude.toString()
//        })
//
//        viewModel.getWeatherData("28.70", "77.1", API_ID)
//        viewModel.response.observe(viewLifecycleOwner, Observer {
//            binding.temperature.text = "Temp: ${it.main.temp}"
//        })

        GlobalScope.launch(Dispatchers.Main) {

            viewModel.getLocationData().observe(viewLifecycleOwner, Observer {
                latitude =  it.latitude.toString()
                longitude = it.longitude.toString()
            })

            delay(2000L)

            var flag = 0

            if (WeatherApplication.hasNetwork())
            {
                if (latitude.isBlank() && longitude.isBlank() )
                {
                    viewModel.readOfflineData.observe(viewLifecycleOwner, Observer {
                        if (it.isEmpty())
                            Toast.makeText(context,"Error retrieving your location", Toast.LENGTH_LONG).show()
                        else
                        {
                            latitude = it[0].lat
                            longitude = it[0].lon

                            flag = 999

                            Toast.makeText(context,"Cord from db", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

                if (flag == 999)
                {
                    delay(1000L)
                }

                viewModel.getWeatherData(latitude, longitude, API_ID)

                viewModel.response.observe(viewLifecycleOwner, Observer {

                    binding.cordinates.text = "Lat: $latitude Lon: $longitude"
                    binding.humidity.text = "Humidity: ${it.main.humidity}"
                    binding.maxMinTemp.text = "Max Temp: ${it.main.temp_max}  Min Temp: ${it.main.temp_min}"
                    binding.pressure.text = "Pressure: ${it.main.pressure}"
                    binding.temperature.text = "Temperature: ${it.main.temp}"
                    binding.sunRiseSet.text = "Sunrise: ${it.sys.sunrise}  Sunset: ${it.sys.sunset}"
                    binding.weatherDesc.text = it.weather[0].description
                    binding.visibility.text = "Visibility: ${it.visibility}"
                    binding.wind.text = "Wind: ${it.wind.speed}"

                    val currentTime = System.currentTimeMillis()
                    val data = WeatherDbModel(
                        10,
                        currentTime,
                        it.main.temp,
                        it.wind.speed,
                        it.main.pressure,
                        latitude,
                        longitude,
                        it.visibility,
                        it.main.humidity
                    )

                    viewModel.addWeatherData(data)
                    Toast.makeText(context, "Added data to room", Toast.LENGTH_LONG).show()
                })
            }
            else
            {
                Toast.makeText(context, "No internet", Toast.LENGTH_LONG).show()
                viewModel.readOfflineData.observe(viewLifecycleOwner, Observer {
                    binding.cordinates.text = "Lat: ${it[0].lat} Lon: ${it[0].lon}"
                    binding.humidity.text = "Humidity: ${it[0].humidity}"
                    //binding.maxMinTemp.text = "Max Temp: ${it.main.temp_max}  Min Temp: ${it.main.temp_min}"
                    binding.pressure.text = "Pressure: ${it[0].pressure}"
                    binding.temperature.text = "Temperature: ${it[0].temp}"
                    //binding.sunRiseSet.text = "Sunrise: ${it.sys.sunrise}  Sunset: ${it.sys.sunset}"
                    //binding.weatherDesc.text = it.weather[0].description
                    binding.visibility.text = "Visibility: ${it[0].visibility}"
                    binding.wind.text = "Wind: ${it[0].wind}"
                })
            }

        }

    }

//    suspend fun startLocationUpdates() {
//        viewModel.getLocationData().observe(viewLifecycleOwner, Observer {
//            binding.latitude.text = it.latitude
//            binding.longitude.text = it.longitude
//
//            val dateFormat = SimpleDateFormat("hh:mm a")
//            dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
//            val timeCal = Calendar.getInstance().time
//            val time = dateFormat.format(timeCal)
//
//            binding.time.text = getTime(it.time!!)
//        })
//    }

    private fun getTime(timeStamp: String): String? {
        val calendar = Calendar.getInstance()
        val timeZone = calendar.timeZone

        val sdf = SimpleDateFormat("dd/mm/yyyy hh:mm:ss")
        sdf.timeZone = timeZone
        val timeLong = timeStamp.toLong()
        return sdf.format(timeLong*1000L)

    }

}