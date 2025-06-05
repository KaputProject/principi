package ui.dataClasses.transaction

import kotlinx.serialization.SerialName
import ui.dataClasses.locations.Location
import ui.dataClasses.account.AccountInfo
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    @SerialName("_id") val id: String, val user: String,
    val account: AccountInfo,
    val location: Location?,
    val datetime: String,
    val description: String,
    val change: Double,
    val outgoing: Boolean,
    val reference: String? = null
)





