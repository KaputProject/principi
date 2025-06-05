package ui.pages.locationPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.createLocation
import ui.dataClasses.locations.Location
import ui.dataClasses.user.User

@Composable
fun LocationCreate(
    user: User, onBackClick: () -> Unit, onLocationCreated: (Location) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var identifier by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf("") }
    var lng by remember { mutableStateOf("") }

    var message by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Ustvari novo lokacijo", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Ime") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text("Identifikator") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Opis") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Naslov") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = lat,
            onValueChange = { lat = it },
            label = { Text("Zemljepisna širina") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = lng,
            onValueChange = { lng = it },
            label = { Text("Zemljepisna dolžina") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val result = createLocation(
                        userId = user.id.toString(),
                        name = name,
                        identifier = identifier,
                        description = description,
                        address = address,
                        lat = lat.toDoubleOrNull(),
                        lng = lng.toDoubleOrNull()
                    )
                    result.onSuccess {
                        // Po uspehu ustvari Location objekt (lahko je samo minimalen)
                        val newLocation = Location(
                            _id = "generatedId", // lahko nadomestiš s pravim ID iz odgovora, če ga imaš
                            name = name,
                            identifier = identifier,
                            description = description,
                            address = address,
                            lat = lat.toDoubleOrNull(),
                            lng = lng.toDoubleOrNull()
                        )
                        onLocationCreated(newLocation)
                    }
                    result.onFailure {
                        message = "Napaka pri ustvarjanju lokacije: ${it.message}"
                    }
                }
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ustvari")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBackClick, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.secondary,
            ),
        ) {
            Text("Nazaj")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = if (it.startsWith("Napaka")) MaterialTheme.colors.error else MaterialTheme.colors.primary)
        }
    }
}
