package com.example.myweatherapp.model.stats

data class AllTimeStats(
    val avgScoreToPar: Double = 0.0, // Renamed for clarity
    val par3Avg: Double = 0.0,
    val par4Avg: Double = 0.0,
    val par5Avg: Double = 0.0,
    val avgPutts: Double = 0.0,
    val fairwayHitPercentage: Double = 0.0,
    val girPercentage: Double = 0.0
)
