package com.example.myweatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.database.AppDatabase
import com.example.myweatherapp.database.SavedScorecard
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SavedScorecardsViewModel(application: Application) : AndroidViewModel(application) {

    private val savedScorecardDao = AppDatabase.getDatabase(application).savedScorecardDao()

    val savedScorecards: StateFlow<List<SavedScorecard>> = savedScorecardDao.getAllScorecards()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getScorecardById(id: Long): StateFlow<SavedScorecard?> {
        return savedScorecards.map { scorecards ->
            scorecards.find { it.id.toLong() == id }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }
}
