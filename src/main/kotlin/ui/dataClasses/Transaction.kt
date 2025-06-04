package ui.dataClasses


import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: String? = null,
    val user: String? = null,
    val account: String? = null,
    val location: String? = null,
    val datetime: String? = null,
    val description: String? = null,
    val change: Double = 0.0,
    val outgoing: Boolean = true,
    val reference: Long? = null
)

