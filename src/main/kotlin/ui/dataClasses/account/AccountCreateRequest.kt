package ui.dataClasses.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountCreateRequest(
    val userId: String,
    val iban: String,
    val currency: String,
    val balance: Double
)
