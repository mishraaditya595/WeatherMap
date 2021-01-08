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


        GlobalScope.launch(Dispatchers.Main) {

            val systemTime = System.currentTimeMillis()
            var lat_db: String? = ""
            var lon_db: String? = ""
            var time_db: Long = 0L
            viewModel.readOfflineData.observe(viewLifecycleOwner, Observer {
                if (!it.isEmpty())
                {
                    lat_db = it[0].lat
                    lon_db = it[0].lat
                    time_db = it[0].time
                }
            })

            delay(1000L)

            if ((systemTime - time_db) < 60000L)
            {
                Toast.makeText(context, "Reloading from db", Toast.LENGTH_SHORT).show()
                viewModel.readOfflineData.observe(viewLifecycleOwner, Observer {
                    if (!it.isEmpty())
                    {
                        binding.locationTV.text = it[0].cityName
                        binding.humidityNumTv.text = "${it[0].humidity} %"
                        binding.pressureNumTv.text = "${it[0].pressure} hPa"
                        binding.tempTV.text = "${it[0].temp} C"
                        binding.visibilityNumTv.text = "${it[0].visibility} "
                        binding.windSpeedNumTv.text = "${it[0].wind} kmph"
                        binding.feelsLikeTv.text = "Feels like ${it[0].feelsLike} C"
                        binding.aqiNumTv.text = it[0].aqi
                        binding.coNumTv.text = it[0].co
                        binding.no2NumTv.text = it[0].no2
                        binding.coarseParticlesNumTv.text = it[0].pm10
                        binding.fineParticlesNumTv.text = it[0].pm2_5
                        binding.weatherDescTv.text = it[0].weatherDescription

                        if (it[0].aqi.isEmpty())
                            Toast.makeText(context, "empty", Toast.LENGTH_SHORT).show()
                    }

                })
            }
            else
            {
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
                        delay(2000L)
                    }

                    viewModel.getWeatherData(latitude, longitude, API_ID)

                    viewModel.getAqiData(latitude, longitude, API_ID)

                    var aqi: String = ""; var co: String = ""; var no2: String = ""; var pm10: String = ""; var pm2_5: String = ""

                    viewModel.aqiResponse.observe(viewLifecycleOwner, Observer {

                        aqi = it.list[0].main.aqi.toString()
                        co = it.list[0].components.co.toString()
                        no2 = it.list[0].components.no2.toString()
                        pm10 = it.list[0].components.pm10.toString()
                        pm2_5 = it.list[0].components.pm2_5.toString()

                        binding.aqiNumTv.text = aqi
                        binding.coNumTv.text = co
                        binding.no2NumTv.text = no2
                        binding.coarseParticlesNumTv.text = it.list[0].components.pm10.toString()
                        binding.fineParticlesNumTv.text = it.list[0].components.pm2_5.toString()
                    })

                    delay(1000L)

                    viewModel.weatherResponse.observe(viewLifecycleOwner, Observer {

                        binding.locationTV.text = it.name
                        binding.humidityNumTv.text = "${it.main.humidity} %"
                        //binding.maxMinTemp.text = "Max Temp: ${it.main.temp_max}  Min Temp: ${it.main.temp_min}"
                        binding.pressureNumTv.text = "${it.main.pressure} hPa"
                        binding.tempTV.text = "${it.main.temp} C"
                        //binding.sunRiseSet.text = "Sunrise: ${it.sys.sunrise}  Sunset: ${it.sys.sunset}"
                        binding.weatherDescTv.text = it.weather[0].description
                        binding.visibilityNumTv.text = "${it.visibility}"
                        binding.windSpeedNumTv.text = "${it.wind.speed} kmph"
                        binding.feelsLikeTv.text = "Feels like ${it.main.feels_like} C"

                        if (aqi.isEmpty())
                            Toast.makeText(context,"Null", Toast.LENGTH_SHORT).show()

                        val currentTime = System.currentTimeMillis()

                        if (it.weather[0].description.isEmpty())
                            Toast.makeText(context, "desc empty", Toast.LENGTH_SHORT).show()

                        val data = WeatherDbModel(
                                10,
                                it.name,
                                it.weather[0].description,
                                currentTime,
                                it.main.temp,
                                it.wind.speed,
                                it.main.pressure,
                                latitude,
                                longitude,
                                it.visibility,
                                it.main.humidity,
                                it.clouds.all,
                                0.0,
                                0.0,
                                it.main.feels_like,
                                binding.aqiNumTv.text as String,
                                co,
                                no2,
                                pm10,
                                pm2_5
                        )

                        viewModel.addWeatherData(data)
                        Toast.makeText(context, "Added data to room", Toast.LENGTH_LONG).show()
                    })
                }
                else
                {
                    Toast.makeText(context, "No internet", Toast.LENGTH_LONG).show()
                    viewModel.readOfflineData.observe(viewLifecycleOwner, Observer {
                        binding.locationTV.text = it[0].cityName
                        binding.humidityNumTv.text = "${it[0].humidity} %"
                        binding.pressureNumTv.text = "${it[0].pressure} hPa"
                        binding.tempTV.text = "${it[0].temp} C"
                        binding.visibilityNumTv.text = "${it[0].visibility} "
                        binding.windSpeedNumTv.text = "${it[0].wind} kmph"
                        binding.feelsLikeTv.text = "Feels like ${it[0].feelsLike} C"
                        binding.aqiNumTv.text = it[0].aqi
                        binding.coNumTv.text = it[0].co
                        binding.no2NumTv.text = it[0].no2
                        binding.coarseParticlesNumTv.text = it[0].pm10
                        binding.fineParticlesNumTv.text = it[0].pm2_5
                    })
                }

            }

        }
    }
}