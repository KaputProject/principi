package ui.api

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import ui.AuthState
import ui.dataClasses.locations.*
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
            val locationsResponse = json.decodeFromString<LocationsResponse>(responseBody)
            locationsResponse.locations
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        emptyList()
    }
}

suspend fun showLocation(
    userId: String,
    locationId: String,
): Result<LocationPopulated> {
    return try {
        val response = client.get("$url/locations/$locationId") {
            AuthState.token?.let { token ->
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            setBody(mapOf("userId" to userId))
        }

        if (response.status.isSuccess()) {
            val location = response.body<LocationPopulated>()
            Result.success(location)
        } else {
            Result.failure(Exception("Server error: ${response.bodyAsText()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
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
                LocationUpdateRequest(
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
    userId: String, name: String, identifier: String, description: String, address: String, lat: Double?, lng: Double?
): Result<String> {
    return try {
        val body = LocationCreateRequest(
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
