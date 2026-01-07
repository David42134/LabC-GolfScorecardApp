package com.example.myweatherapp.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoResponse(
    val latitude: Double,
    val longitude: Double,
    @SerialName("hourly_units") val hourlyUnits: HourlyUnits,
    val hourly: Hourly
)

@Serializable
data class HourlyUnits(
    val time: String,
    @SerialName("temperature_2m") val temperature2m: String
)

@Serializable
data class Hourly(
    val time: List<String>,
    @SerialName("temperature_2m") val temperature2m: List<Double>
)
