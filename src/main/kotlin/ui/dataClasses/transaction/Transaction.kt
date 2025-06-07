package ui.dataClasses.transaction

import LocationAsStringOrObjectSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ui.dataClasses.account.AccountInfo
import ui.dataClasses.locations.Location
import ui.components.FlexibleLongAsStringSerializer

@Serializable
data class Transaction(
    @SerialName("_id")
    val id: String,
    val user: String,
    val account: AccountInfo,
    @Serializable(with = LocationAsStringOrObjectSerializer::class)
    val location: Location? = null,
    val datetime: String,
    val description: String? = null,
    val change: Double,
    val outgoing: Boolean,

    @Serializable(with = FlexibleLongAsStringSerializer::class)
    val reference: String? = null
) {
}
