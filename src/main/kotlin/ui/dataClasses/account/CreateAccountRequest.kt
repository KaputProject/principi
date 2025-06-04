package ui.dataClasses.account

import kotlinx.serialization.Serializable

@Serializable
data class CreateAccountRequest(
    val userId: String,
    val iban: String,
    val currency: String,
    val balance: Double
)
