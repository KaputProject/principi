package ui.dataClasses.account

import classes.BigDecimalSerializer
import kotlinx.serialization.Serializable
import ui.dataClasses.Statement
import ui.enums.Currency
import java.math.BigDecimal

@Serializable
data class Account(
    val _id: String,
    val user: String,
    val iban: String,
    val currency: String,
    val balance: Double,
    val statements: List<Statement>,
    val __v: Int
)
