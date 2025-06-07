package ui.dataClasses.statemant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ui.dataClasses.account.Account
import ui.dataClasses.user.User

@Serializable
data class Statement(
    @SerialName("_id") val id: String? = null, val user: User? = null,
    val account: Account? = null,
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
