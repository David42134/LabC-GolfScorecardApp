package com.example.myweatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedScorecardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScorecard(scorecard: SavedScorecard)

    @Query("SELECT * FROM saved_scorecards ORDER BY date DESC")
    fun getAllScorecards(): Flow<List<SavedScorecard>>
}
