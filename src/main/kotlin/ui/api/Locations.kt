package ui.api

import ui.dataClasses.account.CreateAccountRequest
import UpdateAccountRequest
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import ui.AuthState
import ui.dataClasses.account.Account
import ui.dataClasses.account.AccountsResponse
import ui.dataClasses.locations.CreateLocationRequest
import ui.dataClasses.locations.Location
import ui.dataClasses.locations.LocationResponse
import ui.dataClasses.locations.UpdateLocationRequest
import ui.pages.userPages.client

private val dotenv = dotenv()
private val url = dotenv["API_URL"] ?: "http://localhost:5000"
private val json = Json { ignoreUnknownKeys = true }

suspend fun getLocations(userId: String): List<Location> {
    return try {
        val response: HttpResponse = client.get("$url/locations") {
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
            val locationResponse = json.decodeFromString<LocationResponse>(responseBody)
            locationResponse.locations
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        emptyList()
    }
}




suspend fun updateLocation(
    userId: String,
    locationId: String,
    name: String,
    identifier: String,
    description: String,
    address: String,
    lat: Double?,
    lng: Double?
): Result<String> {
    return try {
        val response = client.put("$url/locations/$locationId") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            contentType(ContentType.Application.Json)
            setBody(
                UpdateLocationRequest(
                    userId = userId,
                    name = name,
                    identifier = identifier,
                    description = description,
                    address = address,
                    lat = lat,
                    lng = lng
                )
            )
        }
        if (response.status.isSuccess()) {
            Result.success("Location updated successfully.")
        } else {
            Result.failure(Exception("Server error: ${response.bodyAsText()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
suspend fun createLocation(
    userId: String,
    name: String,
    identifier: String,
    description: String,
    address: String,
    lat: Double?,
    lng: Double?
): Result<String> {
    return try {
        val body = CreateLocationRequest(
            userId = userId,
            name = name,
            identifier = identifier,
            description = description,
            address = address,
            lat = lat,
            lng = lng
        )

        println(">>> Sending location creation request with body: $body")

        val response: HttpResponse = client.post("$url/locations") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        if (response.status.isSuccess()) {
            Result.success("Location successfully created.")
        } else {
            Result.failure(Exception("Server error: ${response.bodyAsText()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}


suspend fun deleteLocation(locationId: String, userId: String): Result<String> {
    return try {
        val response = client.delete("$url/locations/$locationId") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId))
        }

        if (response.status.isSuccess()) {
            Result.success("Location successfully deleted.")
        } else {
            Result.failure(Exception("Server error: ${response.bodyAsText()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
