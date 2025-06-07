import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.getAccounts
import ui.components.cards.AccountCard
import ui.dataClasses.account.Account
import ui.dataClasses.user.User

@Composable
fun Accounts(
    initialUser: User,
    onBackClick: () -> Unit,
    onNavigate: (Account) -> Unit,
    onCreateClick: (User) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(initialUser) {
        coroutineScope.launch {
            try {
                val allAccounts = getAccounts(userId = initialUser.id.toString())
                accounts = allAccounts
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Napaka pri nalaganju računov: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // Scroll state za LazyColumn
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Računi za ${initialUser.name.orEmpty()} ${initialUser.surname.orEmpty()}",
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "",
                    color = colors.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            accounts.isEmpty() -> {
                Text(
                    "Ni računov za tega uporabnika.",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(accounts) { account ->
                            AccountCard(
                                account = account,
                                onClick = { onNavigate(account) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(listState),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onCreateClick(initialUser) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ustvari nov račun")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.secondary,
            ),
        ) {
            Text("Nazaj")
        }
    }
}
