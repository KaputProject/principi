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

fun main() {
    pdfTest()
}

fun pdfTest() {
    val parser = PdfParser()

    val parameters = StatementParameters(file = "scraper/app/src/main/resources/statements/test.pdf")

    val statement = parser.parse(parameters)

    println(statement)
    println("Extracted ${statement.transactions.size} transactions:")
    statement.transactions.forEach { println(it) }
}
