package ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.dataClasses.transaction.TransactionUser

@Composable
fun TransactionCardUser(
    transaction: TransactionUser,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colors
    val amountColor = if (transaction.outgoing) colors.error else colors.secondaryVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .clickable { onClick() },
        elevation = 6.dp,
        backgroundColor = colors.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Opis: ${transaction.description}",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = transaction.datetime.take(10),
                    style = MaterialTheme.typography.body2,
                    color = colors.onSurface.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Znesek: ${if (transaction.outgoing) "-" else "+"}${transaction.change}",
                color = amountColor,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "IBAN: ${transaction.account.iban}",
                style = MaterialTheme.typography.body2,
                color = colors.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

