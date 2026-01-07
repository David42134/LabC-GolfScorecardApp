package com.example.myweatherapp.view.golf

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myweatherapp.model.golf.GolfCourse
import com.example.myweatherapp.model.golf.Location
import com.example.myweatherapp.ui.theme.MyWeatherAppTheme
import com.example.myweatherapp.viewmodel.GolfViewModel

@Composable
fun GolfScreen(golfViewModel: GolfViewModel, onCourseClick: (GolfCourse) -> Unit) {
    val searchResults by golfViewModel.courses
    val recentCourses by golfViewModel.recentCourses.collectAsState(initial = emptyList())
    val searchQuery = golfViewModel.searchQuery.value
    val isLoading = golfViewModel.isLoading.value

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { golfViewModel.searchQuery.value = it },
                label = { Text("Search for a course") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { golfViewModel.searchCourses() }) {
                Text("Search")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { golfViewModel.healthCheck() }, modifier = Modifier.fillMaxWidth()) {
            Text("Health Check")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (searchResults.isNotEmpty()) {
                        item {
                            Text("Search Results", style = MaterialTheme.typography.headlineMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(searchResults) { course ->
                            GolfCourseItem(
                                course = course,
                                onItemClick = { onCourseClick(course) }
                            )
                        }
                    } else if (recentCourses.isNotEmpty()) {
                        item {
                            Text("Recently Viewed", style = MaterialTheme.typography.headlineMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(recentCourses) { course ->
                            GolfCourseItem(
                                course = course,
                                onItemClick = {
                                    golfViewModel.setCourseFromRecent(course)
                                    onCourseClick(course)
                                }
                            )
                        }
                    } else {
                        item { Text("No recent courses and no search results. Try a search!") }
                    }
                }
            }
        }
    }
}

@Composable
fun GolfCourseItem(course: GolfCourse, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable { onItemClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(text = course.clubName, style = MaterialTheme.typography.headlineSmall)
            Text(text = course.courseName, style = MaterialTheme.typography.bodyLarge)
            Text(text = course.location.address ?: "Address not available", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GolfScreenPreview() {
    val mockCourse = GolfCourse(
        id = 34,
        clubName = "Lubbock Country Club",
        courseName = "Lubbock Country Club",
        location = Location(
            address = "124 Golf Course Lane, Murray, KY 42071, USA"
        )
    )
    MyWeatherAppTheme {
        GolfCourseItem(course = mockCourse, onItemClick = {})
    }
}
