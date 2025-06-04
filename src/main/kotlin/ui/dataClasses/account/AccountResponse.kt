package ui.dataClasses.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountsResponse(
    val message: String,
    val accounts: List<Account>
)

