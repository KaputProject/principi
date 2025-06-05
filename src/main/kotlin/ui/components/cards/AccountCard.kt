package ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import ui.dataClasses.account.Account

@Composable
fun AccountCard(
    account: Account,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .clickable(onClick = onClick),
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = colors.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "IBAN: ${account.iban}",
                style = MaterialTheme.typography.h6,
                color = colors.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Valuta: ${account.currency}",
                style = MaterialTheme.typography.body2,
                color = colors.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Stanje: ${account.balance}",
                style = MaterialTheme.typography.body2,
                color = colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

