package ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.dataClasses.statemant.Statement

@Composable
fun StatementCard(
    statement: Statement,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .clickable { onClick() },
        elevation = 8.dp,
        backgroundColor = colors.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Izpisek za mesec: ${statement.month}/${statement.year}",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "IBAN: ${statement.account?.iban ?: "Neznan"}",
                style = MaterialTheme.typography.body2,
                color = colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

