package ui.api

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import ui.AuthState
import ui.dataClasses.statemant.Statement
import ui.dataClasses.statemant.StatementResponse
import ui.dataClasses.statemant.StatementUpdateRequest
import ui.pages.userPages.client

private val dotenv = dotenv()
private val url = dotenv["API_URL"] ?: "http://localhost:5000"
private val json = Json { ignoreUnknownKeys = true }

suspend fun getStatements(userId: String): List<Statement> {
    return try {
        val response: HttpResponse = client.get("$url/statements") {
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

//            println("API response: $responseBody")

            val statementsResponse = json.decodeFromString<StatementResponse>(responseBody)
//
//            // Debug izpis vseh izpiskov kot seznam
            println("Statements fetched: ${statementsResponse.statements}")

            statementsResponse.statements
        } else {
            println("API call failed with status: ${response.status}")
            emptyList()
        }
    } catch (e: Exception) {
        println("Exception while fetching statements: ${e.message}")
        emptyList()
    }
}

suspend fun updateStatement(
    userId: String,
    statementId: String,
    startDate: String,
    endDate: String,
    inflow: Double,
    outflow: Double,
    startBalance: Double,
    endBalance: Double,
    month: Int,
    year: Int
): Result<String> {
    return try {
        val requestBody = StatementUpdateRequest(
            startDate = startDate,
            endDate = endDate,
            inflow = inflow,
            outflow = outflow,
            startBalance = startBalance,
            endBalance = endBalance,
            month = month,
            year = year,
            userId = userId
        )

        println("PUT $url/statements/$statementId")
        println("Headers:")
        AuthState.token?.let { token ->
            println("Authorization: Bearer $token")
        }
        println("Request body: $requestBody")

        val response = client.put("$url/statements/$statementId") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        val responseText = response.bodyAsText()
        println("Response status: ${response.status}")
        println("Response body: $responseText")

        if (response.status.isSuccess()) {
            Result.success("Statement updated successfully.")
        } else {
            Result.failure(Exception("Server error: $responseText"))
        }
    } catch (e: Exception) {
        println("Exception during updateStatement: ${e.message}")
        Result.failure(e)
    }
}

suspend fun deleteStatement(userId: String, statementId: String): Result<String> {
    return try {
        val response = client.delete("$url/statements/$statementId") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId))
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
