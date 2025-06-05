package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable

@Composable
fun Header(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colors.primaryVariant), // barva iz tvoje teme
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Kaput admin panel",
                color = MaterialTheme.colors.onPrimary, // barva teksta iz teme
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = "Spremeni temo",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { onToggleTheme() },
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}


