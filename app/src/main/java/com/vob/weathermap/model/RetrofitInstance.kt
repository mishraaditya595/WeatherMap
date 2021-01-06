package com.vob.weathermap.model

import com.vob.weathermap.WeatherApplication
import com.vob.weathermap.util.Constants.Companion.BASE_URL
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object {

        private val HEADER_CACHE_CONTROL = "Cache-Control"
        private val HEADER_PRAGMA = "Pragma"

        private var instance: RetrofitInstance? = null

        private fun httpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor()

        private fun networkInterceptor(): Interceptor {
            return Interceptor {
                val response = it.proceed(it.request())

                val cacheControl = CacheControl.Builder()
                        .maxAge(60, TimeUnit.SECONDS)
                        .build()

                return@Interceptor response.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                        .build()
            }
        }

        private fun offlineInterceptor(): Interceptor {
            return Interceptor {
                var request = it.request()

                if (WeatherApplication.hasNetwork() == false) {
                    val cacheControl = CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build()

                    request = request.newBuilder()
                            .removeHeader(HEADER_PRAGMA)
                            .removeHeader(HEADER_CACHE_CONTROL)
                            .cacheControl(cacheControl)
                            .build()
                }

                return@Interceptor it.proceed(request)
            }
        }

        private fun cache(): Cache {
            //return Cache(File(WeatherApplication.getInstance()!!.cacheDir, "offlineCache"), cacheSize)
            return Cache(WeatherApplication.getInstance()!!.cacheDir, cacheSize)
        }

        private val cacheSize = (5 * 1024 * 1024).toLong()

    }

    private val retrofit by lazy {

//        val okHttpClient = OkHttpClient.Builder()
//                .cache(cache())
//                .addInterceptor(httpLoggingInterceptor()) //used when network is on & off
//                .addNetworkInterceptor(networkInterceptor()) //used when network is on
//                .addInterceptor(offlineInterceptor())
//                .build()

        val okHttpClient = OkHttpClient.Builder()
                .cache(cache())
                .addInterceptor { chain->
                    val request = chain.request()

                    if (WeatherApplication.hasNetwork())
                        request.newBuilder().header(HEADER_CACHE_CONTROL, "public, max-age=" +60).build()
                    else
                        request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()

                    chain.proceed(request)
                }
                .build()

        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val api: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }

}