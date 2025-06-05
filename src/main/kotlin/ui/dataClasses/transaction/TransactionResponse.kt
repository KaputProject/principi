package ui.dataClasses.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    val message: String,
    val transaction: Transaction
)

