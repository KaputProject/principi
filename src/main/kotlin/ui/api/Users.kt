package ui.api

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import ui.pages.client
import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv()
val url = dotenv["API_URL"] ?: "http://localhost:5000"

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

suspend fun users(): String {
    return try {
        val response: HttpResponse = client.get("${url}/users")
        if (response.status.isSuccess()) response.bodyAsText()
        else "Napaka: ${response.status.value}"
    } catch (e: Exception) {
        "Napaka pri pridobivanju uporabnikov: ${e.message}"
    }
}
