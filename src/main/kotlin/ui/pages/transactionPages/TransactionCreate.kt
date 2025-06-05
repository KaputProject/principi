package ui.pages.transactionPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.createTransaction
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.transaction.TransactionCreate
import ui.dataClasses.account.AccountInfo
import ui.dataClasses.locations.Location
import ui.dataClasses.statemant.Statement
import ui.dataClasses.user.User

@Composable
fun TransactionCreate(  // Naj bo ime z veliko začetnico skladno z običaji
    user: User,
    statement: Statement,
    onBackClick: () -> Unit,
    onTransactionCreated: (Transaction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var locationId by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var change by remember { mutableStateOf("") }
    var outgoing by remember { mutableStateOf(false) }
    var reference by remember { mutableStateOf("") }
    var accountIban by remember { mutableStateOf(statement.account?.iban ?: "") }
    var datetime by remember { mutableStateOf(statement.startDate ?: "") }

    var message by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Ustvari novo transakcijo", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = accountIban,
            onValueChange = { accountIban = it },
            label = { Text("IBAN računa") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.primary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = locationId,
            onValueChange = { locationId = it },
            label = { Text("ID lokacije (neobvezno)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.primary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = datetime,
            onValueChange = { datetime = it },
            label = { Text("Datum in čas (ISO 8601)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.primary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Opis") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.primary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = change,
            onValueChange = { change = it },
            label = { Text("Znesek spremembe") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.primary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = outgoing,
                onCheckedChange = { outgoing = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    uncheckedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    checkmarkColor = MaterialTheme.colors.onPrimary
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Odhodna transakcija", color = MaterialTheme.colors.onSurface)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = reference,
            onValueChange = { reference = it },
            label = { Text("Referenca (neobvezno)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val account = AccountInfo(
                            iban = accountIban,
                            _id = statement.account?._id ?: "",
                        )
                        val location: Location? = if (locationId.isNotBlank()) {
                            Location(
                                _id = locationId,
                                name = "",
                                identifier = "",
                                description = "",
                                address = "",
                                lat = null,
                                lng = null
                            )
                        } else null

                        val transactionCreate = TransactionCreate(
                            userId = user.id.toString(),
                            account = account,
                            location = location,
                            datetime = datetime,
                            description = description,
                            change = change.toDoubleOrNull() ?: 0.0,
                            outgoing = outgoing,
                            reference = if (reference.isBlank()) null else reference
                        )

                        val response = createTransaction(transactionCreate)
                        onTransactionCreated(response.transaction)
                    } catch (e: Exception) {
                        message = "Napaka pri ustvarjanju transakcije: ${e.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ustvari")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.secondary,
                contentColor = colors.onSecondary
            ),
        ) {
            Text("Nazaj")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                it,
                color = if (it.startsWith("Napaka")) MaterialTheme.colors.error else MaterialTheme.colors.primary
            )
        }
    }
}
