package com.example.myweatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_golf_courses")
data class RecentGolfCourse(
    @PrimaryKey val id: Int,
    val viewedAt: Long,
    val courseJson: String // Store the entire GolfCourse object as a JSON string
)
