import java.math.BigDecimal
import java.util.Date
import classes.*

/**
 * The program should receive the following parameters:
 * - The pdf file
 * - The users unique string
 * - An array of known unique strings for users partners
 *
 * Format:
 *  {
 *      "file": "path/to/file.pdf",
 *      "user": "KUDER LUKA",
 *      "partners": [
 *          "LANA K.",
 *          "UNIFIT D.O.O."
 *      ]
 *  }
 */
data class StatementParameters(
    val file: String = "scraper/app/src/main/resources/statements/test.pdf",
    val user: String = "KUDER LUKA",
    val partners: List<String> = listOf(
        "LANA K.",
        "UNIFITNES, D.O.O.",
        "MDDSZ-DRZAVNE STIPENDIJE - ISCSD 2",
        "ASPIRIA d.o.o.",
        "HUMANITARNO DRUŠTVO LIONS KLUB KONJICE",
        "PayPal Europe S.a.r.l. et Cie S.C.A",
        "TELEKOM SLOVENIJE D.D.",
    )
)

data class Transaction(
    val date: String,
    val reference: String,
    val partner: String,
    val description: String,
    val change: BigDecimal,
    val balance: BigDecimal,
)

data class Statement(
    var user: String = "KUDER LUKA",
    var iban: String = "SI56 6100 0000 0000 000",
    var transactions: MutableList<Transaction> = mutableListOf(),
    var startDate: Date? = null,
    var endDate: Date? = null,
    var month: String? = null,

    var startBalance: BigDecimal = BigDecimal.ZERO,
    var endBalance: BigDecimal = BigDecimal.ZERO,
    var inflow: BigDecimal = BigDecimal.ZERO,
    var outflow: BigDecimal = BigDecimal.ZERO,
)

fun main() {
    val parser = PdfParser()
    val transactions = parser.parse(StatementParameters())

    println("Extracted ${transactions.size} transactions:")
    transactions.forEach { println(it) }
}
