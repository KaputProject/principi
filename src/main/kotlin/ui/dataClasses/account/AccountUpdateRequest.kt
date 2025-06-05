import kotlinx.serialization.Serializable

@Serializable
data class UpdateAccountRequest(
    val userId: String,
    val iban: String,
    val currency: String,
    val balance: Double
)
