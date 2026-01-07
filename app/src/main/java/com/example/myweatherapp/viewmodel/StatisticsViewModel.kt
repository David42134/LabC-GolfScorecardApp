package com.example.myweatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.model.stats.AllTimeStats
import com.example.myweatherapp.repository.StatisticsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

enum class StatsFilter {
    ALL_TIME,
    LAST_20_ROUNDS,
    LAST_2_WEEKS
}

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val statisticsRepository = StatisticsRepository(application)

    private val _filter = MutableStateFlow(StatsFilter.ALL_TIME)
    val filter: StateFlow<StatsFilter> = _filter

    val stats: StateFlow<AllTimeStats> = _filter.flatMapLatest { filter ->
        statisticsRepository.getStats(filter)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AllTimeStats()
    )

    fun setFilter(newFilter: StatsFilter) {
        _filter.value = newFilter
    }
}
