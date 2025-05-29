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

fun main() {
    //pdfTest() //dela
    //mapsTest() //dela
    //exchangeRateTest() //dela
    val server = PdfFileTransfer()
    server.startServer()
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
