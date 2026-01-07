package com.example.myweatherapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.GolfCourse
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Golf : Screen("golf", "Golf", Icons.Default.GolfCourse)
    object Saved : Screen("saved", "Saved", Icons.Default.List)
    object Stats : Screen("stats", "Stats", Icons.Default.BarChart)

    companion object {
        const val GOLF_SCORECARD_ROUTE = "golf_scorecard"
        const val GOLF_COURSE_ID_ARG = "courseId"
        const val GOLF_SCORECARD_FULL_ROUTE = "$GOLF_SCORECARD_ROUTE/{$GOLF_COURSE_ID_ARG}"
    }
}
