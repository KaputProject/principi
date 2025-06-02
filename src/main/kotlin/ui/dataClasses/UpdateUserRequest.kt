@kotlinx.serialization.Serializable
data class UpdateUserRequest(
    val userId: String,
    val username: String,
    val password: String? = null,
    val name: String,
    val surname: String,
    val email: String,
    val dateOfBirth: String,
    val isAdmin: Boolean
)
