package ui.dataClasses.user

@kotlinx.serialization.Serializable
data class UserUpdateRequest(
    val userId: String,
    val username: String,
    val password: String? = null,
    val name: String,
    val surname: String,
    val email: String,
    val dateOfBirth: String,
    val isAdmin: Boolean
)
