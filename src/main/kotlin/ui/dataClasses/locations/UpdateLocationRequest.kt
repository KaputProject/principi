package ui.dataClasses.locations

import kotlinx.serialization.Serializable

@Serializable
data class UpdateLocationRequest(
    val userId: String,
    val name: String?,
    val identifier: String?,
    val description: String?,
    val address: String?,
    val lat: Double?,
    val lng: Double?
)
