import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    val message: String, val filename: String? = null, val metadata: String? = null, val ime: String? = null
)
