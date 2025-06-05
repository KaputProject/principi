package ui.pages.accountPages

import DropdownMenuCurrency
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.createAccount
import ui.dataClasses.user.User
import ui.enums.Currency

@Composable
fun AccountCreate(user: User? = null, onBackClick: (() -> Unit)? = null) {
    val coroutineScope = rememberCoroutineScope()
    var iban by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf(Currency.EUR.name) }
    var balance by remember { mutableStateOf("") }

    var message by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Ustvari nov račun", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = iban,
            onValueChange = { iban = it },
            label = { Text("IBAN") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuCurrency(
            selected = currency, onSelect = { currency = it })

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = balance,
            onValueChange = { balance = it },
            label = { Text("Začetno stanje") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    println("REQUEST za ustvarjanje računa:")
                    println("userId: ${user?.id}")
                    println("IBAN: $iban")
                    println("Currency: $currency")
                    println("Balance: ${balance.toDoubleOrNull() ?: 0.0}")
                    val result = createAccount(
                        userId = user?.id ?: return@launch,   // Poskrbi, da user prihaja iz parametra funkcije
                        iban = iban, currency = currency, balance = balance.toDoubleOrNull() ?: 0.0
                    )


                    message = result.fold(onSuccess = { it }, onFailure = { "Napaka: ${it.message}" })
                }
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ustvari")
        }

        onBackClick?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = it,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colors.secondary,
                ),
            ) {
                Text("Nazaj")
            }
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = if (it.startsWith("Napaka")) MaterialTheme.colors.error else MaterialTheme.colors.primary)
        }
    }
}


