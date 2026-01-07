package com.example.myweatherapp.view.golf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myweatherapp.viewmodel.StatisticsViewModel
import com.example.myweatherapp.viewmodel.StatsFilter
import java.text.DecimalFormat

@Composable
fun StatisticsScreen(statisticsViewModel: StatisticsViewModel = viewModel()) {
    val stats by statisticsViewModel.stats.collectAsState()
    val currentFilter by statisticsViewModel.filter.collectAsState()
    val scoreFormat = DecimalFormat("#.##")
    val signedScoreFormat = DecimalFormat("+#.##;-#.##")
    val percentFormat = DecimalFormat("#.##'%'")

    Column(modifier = Modifier.padding(16.dp)) {
        Text("All-Time Statistics", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterButton(text = "All Time", isSelected = currentFilter == StatsFilter.ALL_TIME) {
                statisticsViewModel.setFilter(StatsFilter.ALL_TIME)
            }
            FilterButton(text = "Last 20", isSelected = currentFilter == StatsFilter.LAST_20_ROUNDS) {
                statisticsViewModel.setFilter(StatsFilter.LAST_20_ROUNDS)
            }
            FilterButton(text = "Last 2 Weeks", isSelected = currentFilter == StatsFilter.LAST_2_WEEKS) {
                statisticsViewModel.setFilter(StatsFilter.LAST_2_WEEKS)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        StatCard("Average Score to Par", signedScoreFormat.format(stats.avgScoreToPar))
        StatCard("Par 3 Average", scoreFormat.format(stats.par3Avg))
        StatCard("Par 4 Average", scoreFormat.format(stats.par4Avg))
        StatCard("Par 5 Average", scoreFormat.format(stats.par5Avg))
        StatCard("Average Putts", scoreFormat.format(stats.avgPutts))
        StatCard("Fairway Hit Percentage", percentFormat.format(stats.fairwayHitPercentage))
        StatCard("Greens in Regulation", percentFormat.format(stats.girPercentage))
    }
}

@Composable
fun FilterButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(text)
    }
}

@Composable
fun StatCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
