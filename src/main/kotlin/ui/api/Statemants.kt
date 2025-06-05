package ui.api

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import ui.AuthState
import ui.dataClasses.statemant.Statement
import ui.dataClasses.statemant.StatementResponse
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

            println("API response: $responseBody")

            val statementsResponse = json.decodeFromString<StatementResponse>(responseBody)

            // Debug izpis vseh izpiskov kot seznam
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
