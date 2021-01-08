package com.vob.weathermap.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [WeatherDbModel::class], version = 5, exportSchema = false)
abstract class WeatherDB: RoomDatabase() {

    abstract fun weatherDao(): WeatherDAO

    companion object {
        @Volatile
        private var INSTANCE: WeatherDB? = null

        var migration1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        val migration2_3 = object : Migration(2, 3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'weather_data' ADD COLUMN 'clouds' INTEGER NOT NULL DEFAULT 0; " +
                        "ALTER TABLE 'weather_data' ADD COLUMN 'dewPoints' FLOAT NOT NULL DEFAULT 0.0;" +
                "ALTER TABLE 'weather_data' ADD COLUMN 'uvi' FLOAT NOT NULL DEFAULT 0.0;")
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
                    "weather_data")
                        .fallbackToDestructiveMigration()
                        .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}