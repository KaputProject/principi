package ui.dataClasses.statemant

import kotlinx.serialization.Serializable

@Serializable
data class StatementResponse(
    val statements: List<Statement>
)
