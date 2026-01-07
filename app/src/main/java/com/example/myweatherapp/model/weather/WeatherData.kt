package com.example.myweatherapp.model.weather

import androidx.room.Entity

@Entity(tableName = "weather_data", primaryKeys = ["date", "time"])
data class WeatherData(
    val date: String,
    val time: String,
    val temperature: Double,
    val description: String,
    val icon: String,
    val cloudCoverage: Int
)