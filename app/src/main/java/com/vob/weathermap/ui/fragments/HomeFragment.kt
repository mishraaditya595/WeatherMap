package com.vob.weathermap.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vob.weathermap.R
import com.vob.weathermap.databinding.FragmentHomeBinding
import com.vob.weathermap.viewmodel.HomeViewModel
import com.vob.weathermap.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val activity = requireActivity()
        val viewModelFactory = HomeViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        //viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        GlobalScope.launch(Dispatchers.Main) {
            delay(3000L)
            startLocationUpdates()
        }
    }

    suspend fun startLocationUpdates() {
        viewModel.getLocationData().observe(viewLifecycleOwner, Observer {
            binding.latitude.text = it.latitude
            binding.longitude.text = it.longitude

            val dateFormat = SimpleDateFormat("hh:mm a")
            dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            val timeCal = Calendar.getInstance().time
            val time = dateFormat.format(timeCal)

            binding.time.text = getTime(it.time!!)
        })
    }

    private fun getTime(timeStamp: String): String? {
        val calendar = Calendar.getInstance()
        val timeZone = calendar.timeZone

        val sdf = SimpleDateFormat("dd/mm/yyyy hh:mm:ss")
        sdf.timeZone = timeZone
        val timeLong = timeStamp.toLong()
        return sdf.format(timeLong*1000L)

    }

}