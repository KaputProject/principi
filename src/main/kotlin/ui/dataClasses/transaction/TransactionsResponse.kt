package ui.dataClasses.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionsResponse(
    val message: String,
    val transactions: List<Transaction>
)

