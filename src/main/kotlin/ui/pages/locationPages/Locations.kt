package ui.pages.locationPages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.getLocations
import ui.dataClasses.locations.Location
import ui.dataClasses.user.User
import ui.components.LocationCard

@Composable
fun Locations(
    initialUser: User,
    onBackClick: () -> Unit,
    onNavigate: (Location) -> Unit,
    onCreateClick: (User) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var locations by remember { mutableStateOf<List<Location>>(emptyList()) }

    LaunchedEffect(initialUser) {
        coroutineScope.launch {
            val allLocations = getLocations(userId = initialUser.id.toString())
            println(">>> Initial user ID: ${initialUser.id}")
            locations = allLocations
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Lokacije za ${initialUser.name ?: ""} ${initialUser.surname ?: ""}", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        if (locations.isEmpty()) {
            Text("Ni lokacij za tega uporabnika.")
        } else {
            LazyColumn {
                items(locations) { location ->
                    LocationCard(
                        location = location,
                        onClick = {
                            onNavigate(location)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onCreateClick(initialUser) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Dodaj novo lokacijo")
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nazaj")
        }
    }
}
