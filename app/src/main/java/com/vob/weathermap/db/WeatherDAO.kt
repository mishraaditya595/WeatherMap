package com.vob.weathermap.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addWeatherData(data: WeatherDbModel)

    @Query("SELECT * FROM weather_data ORDER BY id ASC")
    fun readAllData(): LiveData<List<WeatherDbModel>>
}