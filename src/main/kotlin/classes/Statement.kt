package classes

import java.math.BigDecimal

data class Statement(
    var user: String = "KUDER LUKA",
    var iban: String = "SI56 6100 0000 0000 000",
    var startDate: String? = null,
    var endDate: String? = null,

    var startBalance: BigDecimal = BigDecimal.ZERO,
    var endBalance: BigDecimal = BigDecimal.ZERO,
    var inflow: BigDecimal = BigDecimal.ZERO,
    var outflow: BigDecimal = BigDecimal.ZERO,

    var transactions: MutableList<Transaction> = mutableListOf(),
)