package com.vob.weathermap.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [WeatherDbModel::class], version = 2, exportSchema = false)
abstract class WeatherDB: RoomDatabase() {

    abstract fun weatherDao(): WeatherDAO

    companion object {
        @Volatile
        private var INSTANCE: WeatherDB? = null

        var migration = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        @InternalCoroutinesApi
        fun getDatabase(context: Context): WeatherDB{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDB::class.java,
                    "weather_data"
                ).addMigrations(migration).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}