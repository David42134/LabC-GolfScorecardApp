package com.example.myweatherapp.view.golf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myweatherapp.viewmodel.GolfViewModel
import com.example.myweatherapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveScorecardScreen(navController: NavController, golfViewModel: GolfViewModel) {
    val selectedTee = golfViewModel.selectedTee.value
    val course = golfViewModel.selectedCourse.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(course?.courseName ?: "Active Scorecard") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (selectedTee != null) {
            Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Hole", modifier = Modifier.weight(0.6f), fontWeight = FontWeight.Bold)
                            Text("Yardage", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text("Par", modifier = Modifier.weight(0.6f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text("Index", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text("Score", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text("Putts", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text("FH", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text("GIR", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        }
                        HorizontalDivider()
                    }

                    itemsIndexed(selectedTee.holes) { index, hole ->
                        val holeNumber = index + 1
                        val score = golfViewModel.holeScores[holeNumber] ?: ""
                        val putts = golfViewModel.holePutts[holeNumber] ?: ""
                        val fairwayHit = golfViewModel.fairwayHits[holeNumber] ?: false
                        val greenInRegulation = golfViewModel.greensInRegulation[holeNumber] ?: false

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("$holeNumber", modifier = Modifier.weight(0.6f))
                            Text("${hole.yardage}", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            Text("${hole.par}", modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
                            Text("${hole.index ?: "N/A"}", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            TextField(
                                value = score,
                                onValueChange = { golfViewModel.updateScore(holeNumber, it) },
                                modifier = Modifier.weight(0.8f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center)
                            )
                            TextField(
                                value = putts,
                                onValueChange = { golfViewModel.updatePutts(holeNumber, it) },
                                modifier = Modifier.weight(0.8f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center)
                            )
                            Checkbox(
                                checked = fairwayHit,
                                onCheckedChange = { golfViewModel.updateFairwayHit(holeNumber, it) },
                                modifier = Modifier.weight(0.5f)
                            )
                            Checkbox(
                                checked = greenInRegulation,
                                onCheckedChange = { golfViewModel.updateGreenInRegulation(holeNumber, it) },
                                modifier = Modifier.weight(0.5f)
                            )
                        }
                        HorizontalDivider()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        golfViewModel.saveCurrentRound()
                        navController.popBackStack(Screen.Golf.route, false)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Round")
                }
            }
        } else {
            Text("No tee selected. Please go back and select a tee.", modifier = Modifier.padding(innerPadding))
        }
    }
}
