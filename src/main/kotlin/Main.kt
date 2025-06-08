import Database.DatabaseUtil
import java.math.BigDecimal
import java.util.Date
import classes.*
import com.mongodb.client.MongoClients
import exchange.ExchangeRatesApi
import google.*
import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import org.bson.Document
import pdfFileTransfer.PdfFileTransfer
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

//    val server = PdfFileTransfer()
//    server.startServer()
//pdfTest() //dela
//mapsTest() //dela
//exchangeRateTest() //dela
fun main() {
    val parser = PdfParserOtp1()
    val parameters = StatementParameters(
        file = "src/main/resources/uploads/202500333200360_203_20968188 (2).pdf",
        user = "KRAMAR ENEJ",
        partners = listOf("LUKA K.", "KRAMAR ANDREJ", "ZPIZ", "LANE K.", "Roobet")
    )

    val statement = parser.parse(parameters)
    println("IBAN: ${statement.iban}")
    println("Start balance: ${statement.startBalance}")
    println("Transactions parsed: ${statement.transactions.size}")
    statement.transactions.take(5).forEach {
        println(it)
    }
}



fun exchangeRateTest() {
    val exchangeRates = ExchangeRatesApi().get("EUR")

    for (rate in exchangeRates) {
        println("${rate.key}: ${rate.value}")
    }
}

/**
 * Test PDF parsing
 */

fun pdfTest() {
    val parser = PdfParser()

    val parameters = StatementParameters(file = "src/main/resources/statements/test.pdf")

    val statement = parser.parse(parameters)

    println(statement)
    println("Extracted ${statement.transactions.size} transactions:")
    statement.transactions.forEach { println(it) }
}

/**
 * Test Google Maps API
 */

fun mapsTest() {
    val dotenv = dotenv {
        directory = "src/main/resources"
        ignoreIfMissing = true
    }
    val apiKey = dotenv["GOOGLE_API_KEY"] ?: System.getenv("GOOGLE_API_KEY") ?: error("API ključ ni nastavljen.")

    val searcher = MapSearch(apiKey)
    val places = searcher.search("Mango")
    val json = JsonUtils.toJson(places)
    println(json)
}
