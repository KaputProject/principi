package ui.pages.accountPages

import DropdownMenuCurrency
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
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import ui.api.deleteAccount

@Composable
fun AccountEdit(
    user: User,
    initialAccount: Account,
    onBackClick: () -> Unit,
    onAccountUpdated: (Account) -> Unit,
    onAccountDeleted: () -> Unit

) {
    var iban by remember { mutableStateOf(initialAccount.iban) }
    var currency by remember { mutableStateOf(initialAccount.currency) }
    var balance by remember { mutableStateOf(initialAccount.balance.toString()) }

    var message by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

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
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
        ) {
            Text("Izbriši račun", color = MaterialTheme.colors.onError)
        }
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Potrditev brisanja") },
                text = { Text("Ste prepričani, da želite izbrisati ta račun?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            coroutineScope.launch {
                                val result = user.id?.let {
                                    deleteAccount(
                                        accountId = initialAccount._id,
                                        userId = it,
                                    )
                                }

                                message = result?.fold(
                                    onSuccess = { "Račun uspešno izbrisan." },
                                    onFailure = { "Napaka pri brisanju: ${it.message}" }
                                )

                                // Po brisanju lahko samodejno vrneš nazaj
                                if (result?.isSuccess == true) {
                                    onAccountDeleted()
                                }

                            }
                        }
                    ) {
                        Text("Da, izbriši")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Prekliči")
                    }
                }
            )
        }
        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.secondary
            ),
        ) {
            Text("Nazaj")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = if (it.startsWith("Napaka")) MaterialTheme.colors.error else MaterialTheme.colors.primary)
        }
    }
}


