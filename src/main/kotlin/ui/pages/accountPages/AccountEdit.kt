package ui.pages.accountPages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.updateAccount
import ui.dataClasses.account.Account
import ui.dataClasses.user.User
import ui.enums.Currency

import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.runtime.*

@Composable
fun AccountEdit(
    user: User,
    initialAccount: Account,
    onBackClick: () -> Unit,
    onAccountUpdated: (Account) -> Unit
) {
    var iban by remember { mutableStateOf(initialAccount.iban) }
    var currency by remember { mutableStateOf(initialAccount.currency) }
    var balance by remember { mutableStateOf(initialAccount.balance.toString()) }

    var message by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Uredi račun", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = iban,
            onValueChange = { iban = it },
            label = { Text("IBAN") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuCurrency(
            selected = currency,
            onSelect = { currency = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = balance,
            onValueChange = { balance = it },
            label = { Text("Stanje") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val result = user.id?.let {
                        updateAccount(
                            accountId = initialAccount._id,
                            userId = it,
                            iban = iban,
                            currency = currency,
                            balance = balance.toDoubleOrNull() ?: 0.0
                        )
                    }

                    if (result != null) {
                        result.onSuccess {
                            onAccountUpdated(
                                initialAccount.copy(
                                    iban = iban,
                                    currency = currency,
                                    balance = balance.toDoubleOrNull() ?: initialAccount.balance
                                )
                            )
                        }
                    }

                    onBackClick()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Shrani")
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nazaj")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = if (it.startsWith("Napaka")) MaterialTheme.colors.error else MaterialTheme.colors.primary)
        }
    }
}

@Composable
fun DropdownMenuCurrency(
    selected: String,
    onSelect: (String) -> Unit
) {
    val currencies = Currency.entries.map { it.name }
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text("Valuta") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(onClick = {
                    onSelect(currency)
                    expanded = false
                }) {
                    Text(currency)
                }
            }
        }
    }
}
