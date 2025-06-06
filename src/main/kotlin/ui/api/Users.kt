package ui.api

import ui.dataClasses.user.UserUpdateRequest
import ui.dataClasses.user.User
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import ui.pages.userPages.client
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.call.*
import ui.AuthState
import kotlinx.serialization.json.*
import ui.dataClasses.locations.Location

private val dotenv = dotenv()
private val url = dotenv["API_URL"] ?: "http://localhost:5000"
private val json = Json { ignoreUnknownKeys = true }

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

suspend fun showUser(
    userId: String,
): Result<User> {
    return try {
        val response = client.get("$url/users/$userId") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            //setBody(mapOf("userId" to userId))
        }

        if (response.status.isSuccess()) {
            val user = response.body<User>()
            Result.success(user)
        } else {
            Result.failure(Exception("Server error: ${response.bodyAsText()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun deleteUser(userId: String): Result<String> {
    return try {
        val response = client.delete("$url/users/$userId") {
            header("Authorization", "Bearer ${AuthState.token}")
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId)) // <-- telo zahteve
        }

        if (response.status.isSuccess()) {
            Result.success("ui.dataClasses.user.User deleted successfully")
        } else {
            val errorBody = response.bodyAsText()
            Result.failure(Exception("Server error: $errorBody"))
        }
    } catch (e: Exception) {
        Result.failure(e)
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
            json.decodeFromString<List<User>>(responseBody)
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        emptyList()
    }
}

suspend fun updateUser(
    userId: String,
    username: String,
    password: String,
    name: String,
    surname: String,
    email: String,
    dateOfBirth: String,
    isAdmin: Boolean
): Result<String> {
    return try {
        val requestBody = UserUpdateRequest(
            userId = userId,
            username = username,
            password = password.takeIf { it.isNotEmpty() },
            name = name,
            surname = surname,
            email = email,
            dateOfBirth = dateOfBirth,
            isAdmin = isAdmin
        )

        val response = client.put("${url}/users/$userId") {
            header("Authorization", "Bearer ${AuthState.token}")
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        if (response.status.isSuccess()) {
            Result.success("ui.dataClasses.user.User updated successfully")
        } else {
            val errorBody = response.bodyAsText()
            Result.failure(Exception("Server error: $errorBody"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}


