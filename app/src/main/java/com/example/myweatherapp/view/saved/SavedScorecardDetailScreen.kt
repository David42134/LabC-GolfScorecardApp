package com.example.myweatherapp.view.saved

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myweatherapp.database.SavedScorecard
import com.example.myweatherapp.model.golf.TeeType
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScorecardDetailScreen(navController: NavController, scorecard: SavedScorecard) {
    val scores by produceState<Map<Int, String>>(initialValue = emptyMap(), scorecard.scoresJson) {
        value = Json.decodeFromString(scorecard.scoresJson)
    }
    val putts by produceState<Map<Int, String>>(initialValue = emptyMap(), scorecard.puttsJson) {
        value = Json.decodeFromString(scorecard.puttsJson)
    }
    val fairwayHits by produceState<Map<Int, Boolean>>(initialValue = emptyMap(), scorecard.fairwayHitsJson) {
        value = Json.decodeFromString(scorecard.fairwayHitsJson)
    }
    val greensInRegulation by produceState<Map<Int, Boolean>>(initialValue = emptyMap(), scorecard.greensInRegulationJson) {
        value = Json.decodeFromString(scorecard.greensInRegulationJson)
    }
    val tee by produceState<TeeType?>(initialValue = null, scorecard.teeJson) {
        value = Json.decodeFromString(scorecard.teeJson)
    }

    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(scorecard.date))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(scorecard.courseName) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            item {
                Text("Tee: ${scorecard.teeName}", style = MaterialTheme.typography.headlineSmall)
                Text("Date: $date", style = MaterialTheme.typography.bodyLarge)
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hole", modifier = Modifier.weight(0.6f), fontWeight = FontWeight.Bold)
                    Text("Par", modifier = Modifier.weight(0.6f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text("Score", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text("Putts", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text("FH", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text("GIR", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
                HorizontalDivider()
            }

            items(scores.keys.sorted()) { holeNumber ->
                val hole = tee?.holes?.getOrNull(holeNumber - 1)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("$holeNumber", modifier = Modifier.weight(0.6f))
                    Text("${hole?.par ?: '-'}", modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
                    Text(scores[holeNumber] ?: "-", modifier = Modifier.weight(0.8f), textAlign = TextAlign.Center)
                    Text(putts[holeNumber] ?: "-", modifier = Modifier.weight(0.8f), textAlign = TextAlign.Center)
                    Checkbox(
                        checked = fairwayHits[holeNumber] ?: false,
                        onCheckedChange = null, // Read-only
                        modifier = Modifier.weight(0.5f)
                    )
                    Checkbox(
                        checked = greensInRegulation[holeNumber] ?: false,
                        onCheckedChange = null, // Read-only
                        modifier = Modifier.weight(0.5f)
                    )
                }
                HorizontalDivider()
            }
        }
    }
}
