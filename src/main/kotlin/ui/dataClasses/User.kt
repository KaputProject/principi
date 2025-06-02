import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("_id") val id: String? = null,
    val username: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    val dateOfBirth: String? = null,
    val isAdmin: Boolean? = null
)
