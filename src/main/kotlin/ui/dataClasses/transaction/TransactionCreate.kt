package ui.dataClasses.transaction

import ui.dataClasses.account.AccountInfo
import ui.dataClasses.locations.Location
import kotlinx.serialization.Serializable

@Serializable
data class TransactionCreate(
    val userId: String,
    val account: AccountInfo,
    val location: String? = null,
    val datetime: String,
    val description: String,
    val change: Double,
    val outgoing: Boolean,
    val reference: String? = null

)
