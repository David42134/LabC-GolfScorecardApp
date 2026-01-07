package com.example.myweatherapp.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.database.SavedScorecard
import com.example.myweatherapp.model.golf.GolfCourse
import com.example.myweatherapp.model.golf.TeeType
import com.example.myweatherapp.repository.GolfRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GolfViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GolfRepository(application)

    val courses = mutableStateOf<List<GolfCourse>>(emptyList())
    val recentCourses: Flow<List<GolfCourse>> = repository.recentCourses
    val selectedCourse = mutableStateOf<GolfCourse?>(null)
    val selectedTee = mutableStateOf<TeeType?>(null)
    val searchQuery = mutableStateOf("")
    val isLoading = mutableStateOf(false)

    // State for the active scorecard
    val holeScores = mutableStateMapOf<Int, String>()
    val holePutts = mutableStateMapOf<Int, String>()
    val fairwayHits = mutableStateMapOf<Int, Boolean>()
    val greensInRegulation = mutableStateMapOf<Int, Boolean>()

    fun searchCourses() {
        viewModelScope.launch {
            isLoading.value = true
            courses.value = repository.fetchGolfCourses(searchQuery.value)
            isLoading.value = false
        }
    }

    fun setCourseFromRecent(course: GolfCourse) {
        courses.value = listOf(course)
    }

    fun getCourseById(id: Int): GolfCourse? {
        val course = courses.value.find { it.id == id }
        if (course != null) {
            if (course.id != selectedCourse.value?.id) {
                selectedCourse.value = course
                selectedTee.value = course.tees?.male?.firstOrNull() ?: course.tees?.female?.firstOrNull()
            }
            // Add to recents when viewed
            viewModelScope.launch {
                repository.addCourseToRecents(course)
            }
        }
        return course
    }

    fun selectTee(tee: TeeType) {
        selectedTee.value = tee
    }

    fun startActiveRound() {
        holeScores.clear()
        holePutts.clear()
        fairwayHits.clear()
        greensInRegulation.clear()
        selectedTee.value?.holes?.forEachIndexed { index, _ ->
            val holeNumber = index + 1
            holeScores[holeNumber] = ""
            holePutts[holeNumber] = ""
            fairwayHits[holeNumber] = false
            greensInRegulation[holeNumber] = false
        }
    }

    fun updateScore(holeNumber: Int, score: String) {
        if (holeScores.containsKey(holeNumber)) {
            val filteredScore = score.filter { it.isDigit() }
            holeScores[holeNumber] = filteredScore
        }
    }

    fun updatePutts(holeNumber: Int, putts: String) {
        if (holePutts.containsKey(holeNumber)) {
            val filteredPutts = putts.filter { it.isDigit() }
            holePutts[holeNumber] = filteredPutts
        }
    }

    fun updateFairwayHit(holeNumber: Int, isHit: Boolean) {
        if (fairwayHits.containsKey(holeNumber)) {
            fairwayHits[holeNumber] = isHit
        }
    }

    fun updateGreenInRegulation(holeNumber: Int, isHit: Boolean) {
        if (greensInRegulation.containsKey(holeNumber)) {
            greensInRegulation[holeNumber] = isHit
        }
    }

    fun saveCurrentRound() {
        val course = selectedCourse.value ?: return
        val tee = selectedTee.value ?: return

        val scorecard = SavedScorecard(
            courseName = course.courseName,
            teeName = tee.teeName,
            date = System.currentTimeMillis(),
            scoresJson = Json.encodeToString(holeScores.toMap()),
            puttsJson = Json.encodeToString(holePutts.toMap()),
            fairwayHitsJson = Json.encodeToString(fairwayHits.toMap()),
            greensInRegulationJson = Json.encodeToString(greensInRegulation.toMap()),
            teeJson = Json.encodeToString(tee)
        )

        viewModelScope.launch {
            repository.saveScorecard(scorecard)
        }
    }

    fun healthCheck() {
        viewModelScope.launch {
            repository.performHealthCheck()
        }
    }
}
