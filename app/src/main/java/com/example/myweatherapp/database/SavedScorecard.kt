package com.example.myweatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_scorecards")
data class SavedScorecard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courseName: String,
    val teeName: String,
    val date: Long,
    val scoresJson: String, // JSON string of Map<Int, String>
    val puttsJson: String,  // JSON string of Map<Int, String>
    val fairwayHitsJson: String, // JSON string of Map<Int, Boolean>
    val greensInRegulationJson: String, // JSON string of Map<Int, Boolean>
    val teeJson: String // New field to store the full TeeType object as JSON
)
