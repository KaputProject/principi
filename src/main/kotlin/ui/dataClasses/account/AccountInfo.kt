package ui.dataClasses.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountInfo(
    val iban: String,
    val _id: String? = null
)
fun Account.toAccountInfo(): AccountInfo {
    return AccountInfo(
        iban = this.iban,
        _id = this._id
    )
}
