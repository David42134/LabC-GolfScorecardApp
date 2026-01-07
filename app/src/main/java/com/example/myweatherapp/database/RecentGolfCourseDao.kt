package com.example.myweatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentGolfCourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentCourse(course: RecentGolfCourse)

    @Query("SELECT * FROM recent_golf_courses ORDER BY viewedAt DESC")
    fun getRecentCourses(): Flow<List<RecentGolfCourse>>

    @Query("DELETE FROM recent_golf_courses WHERE id NOT IN (SELECT id FROM recent_golf_courses ORDER BY viewedAt DESC LIMIT 10)")
    suspend fun trimRecentCourses()
}
