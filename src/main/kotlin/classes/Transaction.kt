package classes

import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Transaction(
    val date: String,
    val reference: String,
    val partner: String,
    val description: String,

    @Serializable(with = BigDecimalSerializer::class)
    val change: BigDecimal,

    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal,

    var outgoing: Boolean? = null,
    var known_partner: Boolean? = null,
)
