package com.example.myweatherapp.repository

import android.app.Application
import com.example.myweatherapp.database.AppDatabase
import com.example.myweatherapp.model.golf.TeeType
import com.example.myweatherapp.model.stats.AllTimeStats
import com.example.myweatherapp.viewmodel.StatsFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.util.Calendar

class StatisticsRepository(application: Application) {

    private val savedScorecardDao = AppDatabase.getDatabase(application).savedScorecardDao()

    fun getStats(filter: StatsFilter): Flow<AllTimeStats> {
        return savedScorecardDao.getAllScorecards().map { allScorecards ->
            val filteredScorecards = when (filter) {
                StatsFilter.ALL_TIME -> allScorecards
                StatsFilter.LAST_20_ROUNDS -> allScorecards.take(20)
                StatsFilter.LAST_2_WEEKS -> {
                    val twoWeeksAgo = Calendar.getInstance().apply {
                        add(Calendar.WEEK_OF_YEAR, -2)
                    }.timeInMillis
                    allScorecards.filter { it.date >= twoWeeksAgo }
                }
            }

            if (filteredScorecards.isEmpty()) return@map AllTimeStats()

            val scoreToParList = mutableListOf<Int>()
            var totalPutts = 0
            var totalFairwayHits = 0
            var totalGreensInRegulation = 0
            var par3Score = 0
            var par3Count = 0
            var par4Score = 0
            var par4Count = 0
            var par5Score = 0
            var par5Count = 0
            var holeCount = 0

            filteredScorecards.forEach { scorecard ->
                val scores: Map<Int, String> = Json.decodeFromString(scorecard.scoresJson)
                val putts: Map<Int, String> = Json.decodeFromString(scorecard.puttsJson)
                val fairwayHits: Map<Int, Boolean> = Json.decodeFromString(scorecard.fairwayHitsJson)
                val greensInRegulation: Map<Int, Boolean> = Json.decodeFromString(scorecard.greensInRegulationJson)
                val tee: TeeType = Json.decodeFromString(scorecard.teeJson)

                var roundScore = 0
                var roundPar = 0

                scores.forEach { (holeNumber, scoreStr) ->
                    if (scoreStr.isNotBlank()) {
                        val score = scoreStr.toInt()
                        roundScore += score
                        holeCount++

                        totalPutts += putts[holeNumber]?.toIntOrNull() ?: 0
                        if (fairwayHits[holeNumber] == true) totalFairwayHits++
                        if (greensInRegulation[holeNumber] == true) totalGreensInRegulation++

                        val hole = tee.holes.getOrNull(holeNumber - 1)
                        if (hole != null) {
                            roundPar += hole.par
                            when (hole.par) {
                                3 -> {
                                    par3Score += score
                                    par3Count++
                                }
                                4 -> {
                                    par4Score += score
                                    par4Count++
                                }
                                5 -> {
                                    par5Score += score
                                    par5Count++
                                }
                            }
                        }
                    }
                }
                if (roundPar > 0) {
                    scoreToParList.add(roundScore - roundPar)
                }
            }
            
            AllTimeStats(
                avgScoreToPar = if (scoreToParList.isNotEmpty()) scoreToParList.average() else 0.0,
                par3Avg = if (par3Count > 0) par3Score.toDouble() / par3Count else 0.0,
                par4Avg = if (par4Count > 0) par4Score.toDouble() / par4Count else 0.0,
                par5Avg = if (par5Count > 0) par5Score.toDouble() / par5Count else 0.0,
                avgPutts = if (holeCount > 0) totalPutts.toDouble() / holeCount else 0.0,
                fairwayHitPercentage = if (holeCount > 0) (totalFairwayHits.toDouble() / holeCount) * 100 else 0.0,
                girPercentage = if (holeCount > 0) (totalGreensInRegulation.toDouble() / holeCount) * 100 else 0.0
            )
        }
    }
}
