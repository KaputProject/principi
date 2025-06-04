package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.account.Account

@Composable
fun AccountCard(
    account: Account,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "IBAN: ${account.iban}", style = MaterialTheme.typography.body1)
            Text(text = "Valuta: ${account.currency}", style = MaterialTheme.typography.body2)
            Text(text = "Stanje: ${account.balance}", style = MaterialTheme.typography.body2)
        }
    }
}
