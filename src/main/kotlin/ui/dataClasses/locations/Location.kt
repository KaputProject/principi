package ui.dataClasses.locations

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val _id: String,
    val name: String,
    val identifier: String? = null,
    val description: String? = null,
    val address: String? = null,
    val lat: Double? = null,
    val lng: Double? = null
)
