package com.example.myweatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedScorecard::class, RecentGolfCourse::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedScorecardDao(): SavedScorecardDao
    abstract fun recentGolfCourseDao(): RecentGolfCourseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
