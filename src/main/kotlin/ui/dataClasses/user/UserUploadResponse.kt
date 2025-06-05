package ui.dataClasses.user

import kotlinx.serialization.Serializable

@Serializable
data class UserUploadResponse(
    val message: String, val filename: String? = null, val metadata: String? = null, val ime: String? = null
)
