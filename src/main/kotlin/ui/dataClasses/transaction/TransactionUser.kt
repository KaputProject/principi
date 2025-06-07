package ui.dataClasses.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ui.dataClasses.account.AccountInfo
import ui.dataClasses.locations.Location
import ui.components.FlexibleLongAsStringSerializer
import ui.dataClasses.user.User

@Serializable
data class TransactionUser(
    @SerialName("_id")
    val id: String,
    val user: User,
    val account: AccountInfo,
    val location: Location? = null,
    val datetime: String,
    val description: String,
    val change: Double,
    val outgoing: Boolean,

    @Serializable(with = FlexibleLongAsStringSerializer::class)
    val reference: String? = null
) {
    fun toTransaction(): Transaction {
        return Transaction(
            id = id,
            user = user.id.toString(),
            account = account,
            location = location,
            datetime = datetime,
            description = description,
            change = change,
            outgoing = outgoing,
            reference = reference
        )
    }
}
