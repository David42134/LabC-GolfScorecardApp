package com.example.myweatherapp.repository

import android.app.Application
import android.util.Log
import com.example.myweatherapp.database.AppDatabase
import com.example.myweatherapp.database.RecentGolfCourse
import com.example.myweatherapp.database.SavedScorecard
import com.example.myweatherapp.model.golf.GolfCourse
import com.example.myweatherapp.model.golf.GolfCourseResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GolfRepository(application: Application) {

    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
            connectTimeoutMillis = 60000
            socketTimeoutMillis = 60000
        }
    }
    private val json = Json { ignoreUnknownKeys = true }
    private val savedScorecardDao = AppDatabase.getDatabase(application).savedScorecardDao()
    private val recentGolfCourseDao = AppDatabase.getDatabase(application).recentGolfCourseDao()

    // The flow now deserializes the JSON string back into a full GolfCourse object.
    val recentCourses: Flow<List<GolfCourse>> = recentGolfCourseDao.getRecentCourses().map { recentCourses ->
        recentCourses.map { json.decodeFromString<GolfCourse>(it.courseJson) }
    }

    suspend fun fetchGolfCourses(query: String): List<GolfCourse> {
        val url = URLBuilder("https://api.golfcourseapi.com/v1/search").apply {
            if (query.isNotBlank()) {
                parameters.append("search_query", query)
            }
        }.build()

        return try {
            val responseString = client.get(url) {
                header("Authorization", "Key T4HAEMQKS5NECNO2XI6NWFFWCI")
            }.bodyAsText()
            Log.d("GolfRepository", "Search Response: $responseString")

            val response: GolfCourseResponse = json.decodeFromString(responseString)
            response.courses
        } catch (e: Exception) {
            Log.e("GolfRepository", "Failed to fetch golf courses", e)
            emptyList()
        }
    }

    suspend fun addCourseToRecents(course: GolfCourse) {
        val courseJson = json.encodeToString(course)
        val recentCourse = RecentGolfCourse(
            id = course.id,
            viewedAt = System.currentTimeMillis(),
            courseJson = courseJson
        )
        recentGolfCourseDao.insertRecentCourse(recentCourse)
        recentGolfCourseDao.trimRecentCourses()
    }

    suspend fun saveScorecard(scorecard: SavedScorecard) {
        savedScorecardDao.insertScorecard(scorecard)
    }

    suspend fun performHealthCheck() {
        val url = "https://api.golfcourseapi.com/v1/healthcheck"
        try {
            val responseString = client.get(url) {
                header("Authorization", "Key T4HAEMQKS5NECNO2XI6NWFFWCI")
            }.bodyAsText()
            Log.d("GolfRepository", "Health Check Response: $responseString")
        } catch (e: Exception) {
            Log.e("GolfRepository", "Health check failed", e)
        }
    }
}
