package ui.dataClasses.locations

import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val locations: List<Location>
)
