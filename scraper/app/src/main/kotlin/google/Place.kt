package google

import java.math.BigDecimal

data class Location(
    val lat: BigDecimal,
    val lng: BigDecimal
)

class Place (
    var name: String = "",
    var address: String = "",
    var location: Location,
    var icon: String = "",
    var types: List<String> = emptyList(),
    var rating: BigDecimal = BigDecimal.ZERO,
    var userRatingsTotal: Int = 0,
    var openingHours: List<String> = emptyList()
){
    override fun toString(): String {
        return """
            Ime: $name
            Naslov: $address
            Lokacija: lat=${location.lat}, lng=${location.lng}
            Ocena: $rating ($userRatingsTotal ocen)
            Tipi: ${types.joinToString(", ")}
            Delovni čas: ${openingHours.joinToString(" | ")}
        """.trimIndent()
    }
}