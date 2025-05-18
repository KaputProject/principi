package exchange

import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.math.BigDecimal

class ExchangeRatesApi {

    private val client = OkHttpClient()

    /**
     * Get exchange rate for a given currency.
     */
    fun get(base: String): MutableMap<String, BigDecimal> {
        val dotenv = dotenv {
            directory = "scraper"
        }

        val key = dotenv["EXCHANGE_RATE_API_KEY"] ?: System.getenv("EXCHANGE_RATE_API_KEY") ?: error("EXCHANGE_RATE_API_KEY is not set.")

        val url = "https://v6.exchangerate-api.com/v6/${key}/latest/${base}"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Unexpected response code: ${response.code}")
            }

            val body = response.body?.string() ?: throw Exception("Empty response body")

            val json = JSONObject(body)
            val ratesJson = json.getJSONObject("conversion_rates")

            val ratesMap = mutableMapOf<String, BigDecimal>()
            for (key in ratesJson.keys()) {
                ratesMap[key] = BigDecimal(ratesJson.getDouble(key))
            }

            return ratesMap
        }
    }
}