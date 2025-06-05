import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.getLocations
import ui.dataClasses.locations.Location
import ui.dataClasses.user.User
import ui.components.cards.LocationCard

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

    // Scroll state za LazyColumn
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            "Lokacije za ${initialUser.name ?: ""} ${initialUser.surname ?: ""}",
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (locations.isEmpty()) {
            Text("Ni lokacij za tega uporabnika.")
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(locations) { location ->
                        LocationCard(
                            location = location,
                            onClick = { onNavigate(location) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(listState),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
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
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.secondary,
            ),
        ) {
            Text("Nazaj")
        }
    }
}
