package ui.dataClasses.locations

import kotlinx.serialization.Serializable

@Serializable
data class LocationsResponse(
    val locations: List<Location>
)
