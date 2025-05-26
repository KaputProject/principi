package google

import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.JsonParser
import java.math.BigDecimal

class MapSearch(private val apiKey: String) {

    fun search(query: String): List<Place> {
        val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
        val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=$encodedQuery&key=$apiKey"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            val json = response.body?.string() ?: return emptyList()
            val root = JsonParser.parseString(json).asJsonObject
            if (!root.has("results")) return emptyList()

            val results = root.getAsJsonArray("results")
            val places = mutableListOf<Place>()

            for (result in results) {
                val obj = result.asJsonObject

                val name = obj.get("name")?.asString ?: ""
                val address = obj.get("formatted_address")?.asString ?: ""

                val locationObj = obj.getAsJsonObject("geometry").getAsJsonObject("location")
                val lat = locationObj.get("lat").asBigDecimal
                val lng = locationObj.get("lng").asBigDecimal

                val icon = obj.get("icon")?.asString ?: ""
                val types = obj.getAsJsonArray("types")?.map { it.asString } ?: emptyList()
                val rating = obj.get("rating")?.asBigDecimal ?: BigDecimal.ZERO
                val userRatingsTotal = obj.get("user_ratings_total")?.asInt ?: 0
                val openingHours = obj.getAsJsonObject("opening_hours")
                    ?.getAsJsonArray("weekday_text")?.map { it.asString } ?: emptyList()

                val place = Place(name, address, Location(lat, lng), icon, types, rating, userRatingsTotal, openingHours)
                places.add(place)
            }

            return places
        }
    }
}
