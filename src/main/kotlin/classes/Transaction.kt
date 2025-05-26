package classes

import java.math.BigDecimal

data class Transaction(
    val date: String,
    val reference: String,
    val partner: String,
    val description: String,
    val change: BigDecimal,
    val balance: BigDecimal,

    var outgoing: Boolean? = null,
    var known_partner: Boolean? = null,
)