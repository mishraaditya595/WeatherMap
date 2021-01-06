package com.vob.weathermap

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class WeatherApplication: Application() {
    
    companion object {
        private var instance: WeatherApplication? = null
        
        public fun getInstance(): WeatherApplication? = instance
        
        public fun hasNetwork() = instance!!.isNetworkConnected()
    }

    override fun onCreate() {
        super.onCreate()

        if (instance == null)
            instance = this
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = cm.activeNetwork?: return false
            val capabilities = cm.getNetworkCapabilities(activeNetwork)?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        return false
    }
}