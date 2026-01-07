package com.example.myweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myweatherapp.navigation.Screen
import com.example.myweatherapp.navigation.view.BottomNavigationBar
import com.example.myweatherapp.ui.theme.MyWeatherAppTheme
import com.example.myweatherapp.view.golf.ActiveScorecardScreen
import com.example.myweatherapp.view.golf.GolfScorecardScreen
import com.example.myweatherapp.view.golf.GolfScreen
import com.example.myweatherapp.view.saved.SavedScorecardDetailScreen
import com.example.myweatherapp.view.saved.SavedScorecardsScreen
import com.example.myweatherapp.view.golf.StatisticsScreen
import com.example.myweatherapp.viewmodel.GolfViewModel
import com.example.myweatherapp.viewmodel.SavedScorecardsViewModel
import com.example.myweatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWeatherAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val golfViewModel: GolfViewModel = viewModel()
    val weatherViewModel: WeatherViewModel = viewModel()
    val savedScorecardsViewModel: SavedScorecardsViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController, startDestination = Screen.Golf.route) {
                composable(Screen.Golf.route) {
                    GolfScreen(golfViewModel = golfViewModel) {
                        navController.navigate("${Screen.GOLF_SCORECARD_ROUTE}/${it.id}")
                    }
                }
                composable(
                    route = Screen.GOLF_SCORECARD_FULL_ROUTE,
                    arguments = listOf(navArgument(Screen.GOLF_COURSE_ID_ARG) { type = NavType.IntType })
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getInt(Screen.GOLF_COURSE_ID_ARG)
                    val course = courseId?.let { golfViewModel.getCourseById(it) }
                    if (course != null) {
                        GolfScorecardScreen(
                            navController = navController,
                            golfViewModel = golfViewModel,
                            weatherViewModel = weatherViewModel,
                            course = course,
                            onStartActiveScorecard = {
                                golfViewModel.startActiveRound()
                                navController.navigate("active_scorecard")
                            }
                        )
                    }
                }
                composable("active_scorecard") {
                    ActiveScorecardScreen(navController = navController, golfViewModel = golfViewModel)
                }
                composable(Screen.Saved.route) {
                    SavedScorecardsScreen(savedScorecardsViewModel = savedScorecardsViewModel) {
                        navController.navigate("saved_scorecard_detail/${it.id}")
                    }
                }
                composable(
                    route = "saved_scorecard_detail/{scorecardId}",
                    arguments = listOf(navArgument("scorecardId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val scorecardId = backStackEntry.arguments?.getLong("scorecardId")
                    if (scorecardId != null) {
                        val scorecard by savedScorecardsViewModel.getScorecardById(scorecardId).collectAsState()
                        scorecard?.let {
                            SavedScorecardDetailScreen(navController = navController, scorecard = it)
                        }
                    }
                }
                composable(Screen.Stats.route) { // Add the new stats screen route
                    StatisticsScreen()
                }
            }
        }
    }
}
