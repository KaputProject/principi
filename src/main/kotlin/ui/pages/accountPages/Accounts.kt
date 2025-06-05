package ui.pages.accountPages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.getAccounts
import ui.dataClasses.account.Account
import ui.dataClasses.user.User
import ui.components.cards.AccountCard

@Composable
fun Accounts(
    initialUser: User,
    onBackClick: () -> Unit,
    onNavigate: (Account) -> Unit,
    onCreateClick: (User) -> Unit

) {
    val coroutineScope = rememberCoroutineScope()
    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }

    LaunchedEffect(initialUser) {
        coroutineScope.launch {
            val allAccounts = getAccounts(userId = initialUser.id.toString())
            println(">>> Initial user ID: ${initialUser.id}")
            accounts = allAccounts
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Računi za ${initialUser.name ?: ""} ${initialUser.surname ?: ""}", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        if (accounts.isEmpty()) {
            Text("Ni računov za tega uporabnika.")
        } else {
            LazyColumn {
                items(accounts) { account ->
                    AccountCard(account = account,
                            onClick = {
                        onNavigate(account)
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onCreateClick(initialUser) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Ustvari nov račun")
        }


        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nazaj")
        }
    }
}
