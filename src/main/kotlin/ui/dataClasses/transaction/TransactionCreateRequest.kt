import kotlinx.serialization.Serializable
import ui.dataClasses.account.AccountInfo
import ui.dataClasses.locations.Location

@Serializable
data class TransactionCreateRequest(
    val userId: String,
    val account: AccountInfo,
    val location: Location? = null,
    val description: String,
    val change: Double,
    val datetime: String,
    val outgoing: Boolean,
    val reference: String? = null
)
