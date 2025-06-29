package ui.dataClasses.transaction

import kotlinx.serialization.Serializable
import ui.dataClasses.account.AccountInfo

@Serializable
data class TransactionCreate(
    val userId: String,
    val account: AccountInfo,
    val location: String? = null,
    val datetime: String,
    val description: String? = null,
    val change: Double,
    val outgoing: Boolean,
    val reference: String? = null

)
