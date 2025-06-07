package ui.dataClasses.locations

import kotlinx.serialization.Serializable
import ui.dataClasses.account.Account
import ui.dataClasses.transaction.Transaction

@Serializable
data class LocationPopulated(
    val _id: String,
    val name: String,
    val identifier: String? = null,
    val description: String? = null,
    val address: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val transaction: Transaction? = null,
    val account: Account? = null,
)
