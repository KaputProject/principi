package ui.api

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import ui.AuthState
import ui.dataClasses.account.Account
import ui.dataClasses.account.AccountsResponse
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.transaction.TransactionResponse
import ui.pages.userPages.client

private val dotenv = dotenv()
private val url = dotenv["API_URL"] ?: "http://localhost:5000"
private val json = Json { ignoreUnknownKeys = true }
suspend fun getTransactions(userId: String): List<Transaction> {
    return try {
        val response: HttpResponse = client.get("$url/transactions") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
                contentType(ContentType.Application.Json)
                setBody(mapOf("userId" to userId))
            }
        }
        if (response.status.isSuccess()) {
            val responseBody = response.bodyAsText()

            // Debug izpis celotnega JSON odgovora
            println("API response: $responseBody")

            val transactionResponse = json.decodeFromString<TransactionResponse>(responseBody)

            // Debug izpis vseh transakcij kot seznam
            println("Transactions fetched: ${transactionResponse.transactions}")

            transactionResponse.transactions
        } else {
            println("API call failed with status: ${response.status}")
            emptyList()
        }
    } catch (e: Exception) {
        println("Exception while fetching transactions: ${e.message}")
        emptyList()
    }
}
