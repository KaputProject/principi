package ui.dataClasses

import kotlinx.serialization.Serializable

@Serializable
data class Statement(
    val id: String? = null,
    val user: String? = null,
    val account: String? = null,
    val transactions: List<String> = emptyList(),
    val startDate: String? = null,
    val endDate: String? = null,
    val inflow: Double = 0.0,
    val outflow: Double = 0.0,
    val startBalance: Double = 0.0,
    val endBalance: Double = 0.0,
    val month: Int = 0,
    val year: Int = 0
)
