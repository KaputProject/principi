package classes

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal
@Serializable
data class Statement(
    var user: String = "KUDER LUKA",
    var iban: String = "SI56 6100 0000 0000 000",
    var startDate: String? = null,
    var endDate: String? = null,

    @Serializable(with = BigDecimalSerializer::class)
    var startBalance: BigDecimal = BigDecimal.ZERO,

    @Serializable(with = BigDecimalSerializer::class)
    var endBalance: BigDecimal = BigDecimal.ZERO,

    @Serializable(with = BigDecimalSerializer::class)
    var inflow: BigDecimal = BigDecimal.ZERO,

    @Serializable(with = BigDecimalSerializer::class)
    var outflow: BigDecimal = BigDecimal.ZERO,


    var transactions: MutableList<Transaction> = mutableListOf(),
)
