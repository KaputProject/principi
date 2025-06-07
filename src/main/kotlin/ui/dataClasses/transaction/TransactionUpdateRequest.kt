package ui.dataClasses.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionUpdateRequest(
    val userId: String,
    val description: String,
    val change: Double,
    val datetime: String,
    val reference: String,
)
