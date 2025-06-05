package ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun UserCard(
    name: String,
    surname: String,
    email: String? = null,
    dateOfBirth: String? = null,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .heightIn(min = 140.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Icon",
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(name, style = MaterialTheme.typography.subtitle1)
            Text(surname, style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            if (!email.isNullOrBlank()) {
                Text(email, style = MaterialTheme.typography.body2, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
            }
            if (!dateOfBirth.isNullOrBlank()) {
                Text("DOB: $dateOfBirth", style = MaterialTheme.typography.body2, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
            }
        }
    }
}
