package ui.pages.locationPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.locations.Location

@Composable
fun ShowLocation(
    location: Location,
    onBackClick: () -> Unit,
    onEditClick: (Location) -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Podrobnosti lokacije", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Ime: ${location.name ?: "Ni podatka"}")
        Text("Identifikator: ${location.identifier ?: "Ni podatka"}")
        Text("Opis: ${location.description ?: "Ni podatka"}")
        Text("Naslov: ${location.address ?: "Ni podatka"}")
        Text("Zem. širina: ${location.lat ?: "Ni podatka"}")
        Text("Zem. dolžina: ${location.lng ?: "Ni podatka"}")
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onEditClick.bind(location),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Uredi")
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nazaj")
        }
    }
}

// Utility za gumb z argumentom
private fun ((Location) -> Unit).bind(location: Location): () -> Unit = {
    this(location)
}
