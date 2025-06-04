package ui.dataClasses

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: String? = null,
    val user: String? = null,
    val transactions: List<String> = emptyList(),
    val name: String? = null,
    val identifier: String? = null,
    val description: String? = null,
    val total_spent: Double = 0.0,
    val total_received: Double = 0.0,
    val address: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val location: GeoPoint = GeoPoint(),
    val icon: String? = null,
    val tags: List<String> = emptyList()
)

@Serializable
data class GeoPoint(
    val type: String = "Point",
    val coordinates: List<Double> = listOf()
)
