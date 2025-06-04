package ui.pages.locationPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.updateLocation
import ui.api.deleteLocation
import ui.dataClasses.locations.Location
import ui.dataClasses.user.User

@Composable
fun LocationEdit(
    user: User,
    initialLocation: Location,
    onBackClick: () -> Unit,
    onLocationUpdated: (Location) -> Unit,
    onLocationDeleted: () -> Unit
) {
    var name by remember { mutableStateOf(initialLocation.name ?: "") }
    var identifier by remember { mutableStateOf(initialLocation.identifier ?: "") }
    var description by remember { mutableStateOf(initialLocation.description ?: "") }
    var address by remember { mutableStateOf(initialLocation.address ?: "") }
    var lat by remember { mutableStateOf(initialLocation.lat?.toString() ?: "") }
    var lng by remember { mutableStateOf(initialLocation.lng?.toString() ?: "") }

    var message by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Uredi lokacijo", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Ime") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = identifier, onValueChange = { identifier = it }, label = { Text("Identifikator") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Opis") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Naslov") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = lat, onValueChange = { lat = it }, label = { Text("Zemljepisna širina") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = lng, onValueChange = { lng = it }, label = { Text("Zemljepisna dolžina") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val result = updateLocation(
                        userId = user.id.toString(),
                        locationId = initialLocation._id,
                        name = name,
                        identifier = identifier,
                        description = description,
                        address = address,
                        lat = lat.toDoubleOrNull(),
                        lng = lng.toDoubleOrNull()
                    )
                    if (result != null) {
                        result.onSuccess {
                            onLocationUpdated(
                                initialLocation.copy(
                                    name = name,
                                    identifier = identifier,
                                    description = description,
                                    address = address,
                                    lat = lat.toDoubleOrNull(),
                                    lng = lng.toDoubleOrNull()
                                )
                            )
                            onBackClick()
                        }
                        result.onFailure {
                            message = "Napaka pri shranjevanju: ${it.message}"
                        }
                    }

                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Shrani")
        }

        Button(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
        ) {
            Text("Izbriši lokacijo", color = MaterialTheme.colors.onError)
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Potrditev brisanja") },
                text = { Text("Ste prepričani, da želite izbrisati to lokacijo?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch {
                            val result = deleteLocation(initialLocation._id)
                            if (result?.isSuccess == true) {
                                message = "Lokacija uspešno izbrisana."
                                onLocationDeleted()
                            } else {
                                message = "Napaka pri brisanju: ${result?.exceptionOrNull()?.message}"
                            }
                        }
                    }) { Text("Da, izbriši") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Prekliči")
                    }
                }
            )
        }

        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
            Text("Nazaj")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = if (it.startsWith("Napaka")) MaterialTheme.colors.error else MaterialTheme.colors.primary)
        }
    }
}
