package com.example.myweatherapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.model.weather.WeatherData
import com.example.myweatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    val weatherData = mutableStateOf<List<WeatherData>?>(null)
    val error = mutableStateOf<String?>(null)
    val latitude = mutableStateOf("58.0")
    val longitude = mutableStateOf("16.0")

    private val repository = WeatherRepository()

    init {
        fetchWeather()
    }

    fun fetchWeather() {
        val lat = latitude.value.toFloatOrNull()
        val lon = longitude.value.toFloatOrNull()

        if (lat == null || lon == null) {
            error.value = "Invalid latitude or longitude"
            return
        }

        viewModelScope.launch {
            try {
                error.value = null // Clear previous errors
                val fetchedData = repository.fetchWeather(lat, lon)
                weatherData.value = fetchedData
            } catch (e: Exception) {
                error.value = "Failed to fetch weather data: ${e.message}"
                weatherData.value = emptyList() // Clear old data on error
            }
        }
    }
}
