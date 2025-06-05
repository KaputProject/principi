package ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.locations.Location

@Composable
fun LocationCard(location: Location, onClick: () -> Unit) {
    val colors = MaterialTheme.colors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp,
        backgroundColor = colors.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = location.name ?: "Neimenovana lokacija",
                style = MaterialTheme.typography.h6,
                color = colors.onSurface
            )
            location.address?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Naslov: $it",
                    style = MaterialTheme.typography.body2,
                    color = colors.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
