package ui.api

import ui.dataClasses.account.AccountCreateRequest
import UpdateAccountRequest
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import ui.AuthState
import ui.dataClasses.account.Account
import ui.dataClasses.account.AccountsResponse
import ui.pages.userPages.client

private val dotenv = dotenv()
private val url = dotenv["API_URL"] ?: "http://localhost:5000"
private val json = Json { ignoreUnknownKeys = true }

suspend fun getAccounts(userId: String): List<Account> {
    return try {
        val response: HttpResponse = client.get("$url/accounts") {
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
            val accountsResponse = json.decodeFromString<AccountsResponse>(responseBody)
            accountsResponse.accounts
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        emptyList()
    }
}


suspend fun createAccount(
    userId: String,
    iban: String,
    currency: String,
    balance: Double
): Result<String> {
    return try {
        val body = AccountCreateRequest(
            userId = userId,
            iban = iban,
            currency = currency,
            balance = balance
        )

        println(">>> Sending account creation request with body: $body")

        val response: HttpResponse = client.post("$url/accounts") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        if (response.status.isSuccess()) {
            Result.success("Account successfully created.")
        } else {
            Result.failure(Exception("Server error: ${response.bodyAsText()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun deleteAccount(accountId: String, userId: String): Result<String> {
    return try {
        val response = client.delete("$url/accounts/$accountId") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId)) // <-- telo zahteve
        }

        if (response.status.isSuccess()) {
            Result.success("Account successfully deleted.")
        } else {
            Result.failure(Exception("Server error: ${response.bodyAsText()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun updateAccount(
    userId: String,
    accountId: String,
    iban: String,
    currency: String,
    balance: Double
): Result<String> {
    return try {
        val response = client.put("$url/accounts/$accountId") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            contentType(ContentType.Application.Json)
            setBody(
                UpdateAccountRequest(
                    userId = userId,
                    iban = iban,
                    currency = currency,
                    balance = balance
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
