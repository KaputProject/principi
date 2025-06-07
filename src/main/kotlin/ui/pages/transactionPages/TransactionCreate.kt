package ui.pages.transactionPages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.createTransaction
import ui.api.getAccounts
import ui.api.getLocations
import ui.components.cards.LocationCard
import ui.dataClasses.account.Account
import ui.dataClasses.account.AccountInfo
import ui.dataClasses.locations.Location
import ui.dataClasses.statemant.Statement
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.transaction.TransactionCreate
import ui.dataClasses.user.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TransactionCreate(
    user: User,
    statement: Statement? = null,
    onBackClick: () -> Unit,
    onTransactionCreated: (Transaction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var locationId by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var change by remember { mutableStateOf("") }
    var outgoing by remember { mutableStateOf(false) }
    var reference by remember { mutableStateOf("") }
    var accountIban by remember { mutableStateOf(statement?.account?.iban ?: "") }
    var datetime by remember { mutableStateOf(statement?.startDate ?: "") }
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var showLocationSelector by remember { mutableStateOf(false) }
    var locations by remember { mutableStateOf<List<Location>>(emptyList()) }
    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }

    var message by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(user.id) {
        locations = getLocations(user.id.toString())
        accounts = getAccounts(user.id.toString())

    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text("Ustvari novo transakcijo", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))

            if (statement == null) {
                Box {
                    OutlinedTextField(
                        value = accountIban,
                        onValueChange = { accountIban = it },
                        label = { Text("IBAN računa") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true },
                        readOnly = true
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        accounts.forEach { account ->
                            DropdownMenuItem(onClick = {
                                accountIban = account.iban
                                expanded = false
                            }) {
                                Text("${account.iban} (${account.currency}, ${account.balance})")
                            }
                        }
                    }
                }
            } else {
                OutlinedTextField(
                    value = accountIban,
                    onValueChange = { accountIban = it },
                    label = { Text("IBAN računa") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Izbrana lokacija: ${selectedLocation?.name ?: "Ni izbrana"}")
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { showLocationSelector = true }) {
                Text("Izberi lokacijo")
            }
            if (showLocationSelector) {
                Column {
                    Text("Izberi lokacijo", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(8.dp))
                    locations.forEach { location ->
                        LocationCard(location = location) {
                            selectedLocation = location
                            showLocationSelector = false
                        }
                    }
                }
            }


            OutlinedTextField(
                value = datetime,
                onValueChange = { datetime = it },
                label = { Text("Datum (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Opis") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = change,
                onValueChange = { change = it },
                label = { Text("Znesek spremembe") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = outgoing, onCheckedChange = { outgoing = it })
                Spacer(modifier = Modifier.width(8.dp))
                Text("Odhodna transakcija")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = reference,
                onValueChange = { reference = it },
                label = { Text("Referenca (neobvezno)") },
                modifier = Modifier.fillMaxWidth()
            )

            message?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    it,
                    color = if (it.startsWith("Napaka")) colors.error else colors.primary
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Potisne gumbe na dno

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                // Tukaj dodaj preverjanje
                                if (accountIban.isBlank()) {
                                    message = "Prosimo, izberi račun."
                                    return@launch
                                }

                                val selectedAccount = accounts.find { it.iban == accountIban }

                                if (selectedAccount == null) {
                                    message = "Napaka: izbran račun ne obstaja."
                                    return@launch
                                }

                                val account = AccountInfo(
                                    iban = selectedAccount.iban,
                                    _id = selectedAccount._id
                                )

                                val locationId: String? = selectedLocation?._id

                                val year = datetime.take(4).toIntOrNull() ?: LocalDate.now().year
                                val inputDate = try {
                                    LocalDate.parse(datetime) // privzema ISO format npr. 2025-06-07
                                } catch (e: Exception) {
                                    message = "Nepravilen datum (pričakovan format: YYYY-MM-DD)"
                                    return@launch
                                }
                                val formattedDate = inputDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

                                val transactionCreate = TransactionCreate(
                                    userId = user.id.toString(),
                                    account = account,
                                    location = locationId,
                                    datetime = formattedDate, // <-- to zamenjamo
                                    description = description,
                                    change = change.toDoubleOrNull() ?: 0.0,
                                    outgoing = outgoing,
                                    reference = if (reference.isBlank()) null else reference,
                                )


                                val response = createTransaction(transactionCreate)
                                onTransactionCreated(response.transaction)
                            } catch (e: Exception) {
                                message = "Napaka pri ustvarjanju transakcije: ${e.message}"
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ustvari")
                }


                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colors.secondary)
                ) {
                    Text("Nazaj")
                }
            }
        }
    }
}

