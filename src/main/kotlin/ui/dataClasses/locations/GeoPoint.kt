package ui.dataClasses.locations

import kotlinx.serialization.Serializable

@Serializable
data class GeoPoint(
    val type: String = "Point",
    val coordinates: List<Double> = listOf()
)