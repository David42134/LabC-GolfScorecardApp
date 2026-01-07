package com.example.myweatherapp.view.golf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myweatherapp.model.golf.GolfCourse
import com.example.myweatherapp.model.golf.Hole
import com.example.myweatherapp.model.golf.TeeType
import com.example.myweatherapp.viewmodel.GolfViewModel
import com.example.myweatherapp.view.weather.WeatherCard
import com.example.myweatherapp.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GolfScorecardScreen(
    navController: NavController,
    golfViewModel: GolfViewModel,
    weatherViewModel: WeatherViewModel,
    course: GolfCourse,
    onStartActiveScorecard: () -> Unit
) {
    val selectedTee = golfViewModel.selectedTee.value
    val allTees = course.tees?.male.orEmpty()
    val weatherData = weatherViewModel.weatherData.value

    // Fetch weather automatically when the screen is first displayed
    LaunchedEffect(course.id) {
        val location = course.location
        if (location.latitude != null && location.longitude != null) {
            weatherViewModel.latitude.value = location.latitude.toString()
            weatherViewModel.longitude.value = location.longitude.toString()
            weatherViewModel.fetchWeather()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(course.courseName) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            Text(text = course.clubName, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = course.location.address ?: "Address not available", style = MaterialTheme.typography.bodyLarge)
            
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onStartActiveScorecard() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) 
            ) {
                Text("Start Active Scorecard")
            }
            
            // Display weather data if available
            weatherData?.let {
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(it) { data ->
                        WeatherCard(data = data)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tee Box Selection Buttons
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allTees) { tee ->
                    Button(
                        onClick = { golfViewModel.selectTee(tee) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (tee == selectedTee) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(tee.teeName)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTee != null) {
                ScorecardTable(tee = selectedTee)
            } else {
                Text("No tee information available for this course.")
            }
        }
    }
}

@Composable
fun ScorecardTable(tee: TeeType) {
    LazyColumn {
        // Header Row
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text("Hole", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Par", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Yardage", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Index", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }
            HorizontalDivider()
        }
        
        // Data Rows
        itemsIndexed(tee.holes) { index, hole ->
            HoleRow(holeNumber = index + 1, hole = hole)
        }
    }
}

@Composable
fun HoleRow(holeNumber: Int, hole: Hole) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text("$holeNumber", modifier = Modifier.weight(1f))
        Text("${hole.par}", modifier = Modifier.weight(1f))
        Text("${hole.yardage}", modifier = Modifier.weight(1f))
        Text("${hole.index ?: "N/A"}", modifier = Modifier.weight(1f))
    }
    HorizontalDivider()
}
