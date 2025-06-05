package ui.dataClasses.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionsResponseUser(
    val message: String,
    val transaction: TransactionUser
)


