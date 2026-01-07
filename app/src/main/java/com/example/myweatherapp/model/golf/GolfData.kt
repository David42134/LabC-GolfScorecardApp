package com.example.myweatherapp.model.golf

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Wrapper for the SEARCH response -> /v1/search
@Serializable
data class GolfCourseResponse(
    val courses: List<GolfCourse>
)

// Represents a GolfCourse. Can be a simpler version from search,
// or a detailed version from /v1/courses/{id}
@Serializable
data class GolfCourse(
    val id: Int,
    @SerialName("club_name") val clubName: String,
    @SerialName("course_name") val courseName: String,
    val location: Location,
    // The 'tees' object is only present in the detailed response
    val tees: Tees? = null
)

@Serializable
data class Location(
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)

@Serializable
data class Tees(
    val female: List<TeeType> = emptyList(),
    val male: List<TeeType> = emptyList()
)

@Serializable
data class TeeType(
    @SerialName("tee_name") val teeName: String,
    @SerialName("total_yards") val totalYards: Int,
    @SerialName("par_total") val parTotal: Int,
    val holes: List<Hole>
)

@Serializable
data class Hole(
    val par: Int,
    val yardage: Int,
    @SerialName("handicap") val index: Int? = null
)
