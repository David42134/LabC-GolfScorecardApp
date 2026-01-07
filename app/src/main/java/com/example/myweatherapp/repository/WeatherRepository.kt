package com.example.myweatherapp.repository

import com.example.myweatherapp.model.weather.OpenMeteoResponse
import com.example.myweatherapp.model.weather.WeatherData
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLBuilder
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherRepository {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchWeather(latitude: Float, longitude: Float): List<WeatherData> {
        val url = URLBuilder("https://api.open-meteo.com/v1/forecast").apply {
            parameters.append("latitude", latitude.toString())
            parameters.append("longitude", longitude.toString())
            parameters.append("hourly", "temperature_2m")
        }.build()

        val responseString = client.get(url).bodyAsText()
        val response: OpenMeteoResponse = json.decodeFromString(responseString)

        val now = LocalDateTime.now()
        val endOfWindow = now.plusHours(24)

        return response.hourly.time.zip(response.hourly.temperature2m)
            .filter { (timeStr, _) ->
                val dateTime = LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                !dateTime.isBefore(now) && dateTime.isBefore(endOfWindow)
            }
            .map { (timeStr, temp) ->
                val dateTime = LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                WeatherData(
                    date = formatDate(dateTime),
                    time = formatTime(dateTime),
                    temperature = temp,
                    description = "", // Open-Meteo basic doesn't provide a text description
                    icon = "", // Open-Meteo basic doesn't provide an icon
                    cloudCoverage = 0 // Placeholder
                )
            }
    }

    private fun formatDate(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("MMM d")
        return dateTime.format(formatter)
    }

    private fun formatTime(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return dateTime.format(formatter)
    }
}
