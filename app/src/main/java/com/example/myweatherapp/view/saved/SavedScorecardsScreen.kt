package com.example.myweatherapp.view.saved

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myweatherapp.database.SavedScorecard
import com.example.myweatherapp.viewmodel.SavedScorecardsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedScorecardsScreen(
    savedScorecardsViewModel: SavedScorecardsViewModel = viewModel(),
    onScorecardClick: (SavedScorecard) -> Unit
) {
    val savedScorecards by savedScorecardsViewModel.savedScorecards.collectAsState()

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(savedScorecards) { scorecard ->
            SavedScorecardItem(scorecard = scorecard, onClick = { onScorecardClick(scorecard) })
        }
    }
}

@Composable
fun SavedScorecardItem(scorecard: SavedScorecard, onClick: () -> Unit) {
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(scorecard.date))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = scorecard.courseName, style = MaterialTheme.typography.headlineSmall)
            Text(text = "Tee: ${scorecard.teeName}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: $date", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
