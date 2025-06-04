package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.locations.Location

@Composable
fun LocationCard(location: Location, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = location.name ?: "Neimenovana lokacija", style = MaterialTheme.typography.h6)
            location.address?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Naslov: $it")
            }
        }
    }
}
