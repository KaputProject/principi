package ui.dataClasses.statemant

import kotlinx.serialization.Serializable

@Serializable
data class StatementUpdateRequest(
    val userId: String,
    val startDate: String,
    val endDate: String,
    val inflow: Double,
    val outflow: Double,
    val startBalance: Double,
    val endBalance: Double,
    val month: Int,
    val year: Int
)
