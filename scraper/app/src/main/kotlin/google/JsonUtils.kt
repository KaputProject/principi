package google

import com.google.gson.Gson

object JsonUtils {
    private val gson = Gson()

    fun <T> toJson(data: T): String {
        return gson.toJson(data)
    }
}