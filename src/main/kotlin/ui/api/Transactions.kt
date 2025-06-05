package ui.api

import TransactionCreateRequest
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import ui.AuthState
import ui.dataClasses.account.AccountInfo
import ui.dataClasses.locations.Location
import ui.dataClasses.transaction.*
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

            val transactionsResponse = json.decodeFromString<TransactionsResponse>(responseBody)

            // Debug izpis vseh transakcij kot seznam
            println("Transactions fetched: ${transactionsResponse.transactions}")

            transactionsResponse.transactions
        } else {
            println("API call failed with status: ${response.status}")
            emptyList()
        }
    } catch (e: Exception) {
        println("Exception while fetching transactions: ${e.message}")
        emptyList()
    }
}

suspend fun updateTransaction(
    userId: String,
    transactionId: String,
    description: String,
    change: Double,
    datetime: String,
    reference: String,
): Result<String> {
    return try {
        val response = client.put("$url/transactions/$transactionId") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            contentType(ContentType.Application.Json)
            setBody(
                TransactionUpdateRequest(
                    userId = userId,
                    description = description,
                    change = change,
                    datetime = datetime,
                    reference = reference
                )
            )
        }

        if (response.status.isSuccess()) {
            Result.success("Account updated successfully.")
        } else {
            Result.failure(Exception("Server error: ${response.bodyAsText()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun showTransaction(
    userId: String,
    transactionId: String,
): Result<TransactionUser> {
    return try {
        val response = client.get("$url/transactions/$transactionId") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId)) // <-- telo zahteve
        }

        if (response.status.isSuccess()) {
            val transactionResponse = response.body<TransactionsResponseUser>()
            Result.success(transactionResponse.transaction)
        } else {
            Result.failure(Exception("Server error: ${response.bodyAsText()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun createTransaction(request: TransactionCreate): TransactionResponse {
    val response = client.post("$url/transactions") {
        AuthState.token?.let { token ->
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        contentType(ContentType.Application.Json)
        setBody(request)
    }
    if (response.status.isSuccess()) {
        return response.body()
    } else {
        throw Exception("Napaka pri kreiranju transakcije: ${response.status}")
    }
}

suspend fun deleteTransaction(userId: String, transactionId: String): Result<String> {
    return try {
        val response = client.delete("$url/transactions/$transactionId") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId)) // <-- telo zahteve
        }

        if (response.status.isSuccess()) {
            Result.success("Transaction successfully deleted.")
        } else {
            Result.failure(Exception("Server error: ${response.bodyAsText()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}