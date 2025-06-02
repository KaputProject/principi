package ui.api

import User
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import ui.pages.client
import io.github.cdimascio.dotenv.dotenv
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ui.AuthState

val dotenv = dotenv()
val url = dotenv["API_URL"] ?: "http://localhost:5000"
val json = Json { ignoreUnknownKeys = true }

suspend fun login(username: String, password: String): String? {
    return try {
        val response: HttpResponse = client.post("$url/users/login") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("username" to username, "password" to password))
        }
        if (response.status.isSuccess()) {
            val responseBody = response.bodyAsText()
            val jsonObject = json.parseToJsonElement(responseBody).jsonObject
            jsonObject["token"]?.jsonPrimitive?.contentOrNull
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

suspend fun createUser(
    username: String, password: String, name: String,
    surname: String, email: String, dateOfBirth: String
): String {
    return try {
        val response: HttpResponse = client.post("${url}/users") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "username" to username,
                    "password" to password,
                    "name" to name,
                    "surname" to surname,
                    "email" to email,
                    "dateOfBirth" to dateOfBirth
                )
            )
        }
        if (response.status.isSuccess()) "Uporabnik uspešno dodan!"
        else "Napaka: ${response.status.value}"
    } catch (e: Exception) {
        "Napaka pri pošiljanju: ${e.message}"
    }
}

suspend fun users(): List<User> {
    return try {
        val response: HttpResponse = client.get("$url/users") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
        }
        if (response.status.isSuccess()) {
            val responseBody = response.bodyAsText()
            json.decodeFromString(responseBody)
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        emptyList()
    }
}

