package ui.dataClasses.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountInfo(
    val _id: String,
    val iban: String
)
