package ui.dataClasses.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountInfo(
    val iban: String,
    val _id: String? = null
)

