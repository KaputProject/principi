package ui.dataClasses.account

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val _id: String,
    val user: String,
    val iban: String,
    val currency: String,
    val balance: Double,
    val statements: List<String>,
)
