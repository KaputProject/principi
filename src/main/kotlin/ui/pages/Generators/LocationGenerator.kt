package ui.pages.Generators

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.launch
import ui.api.createLocation
import java.util.*
import kotlin.random.Random
import ui.components.cards.LocationCard
import ui.dataClasses.locations.Location

@Composable
fun LocationGenerator(userId: String) {
    val faker = remember { Faker() }
    val coroutineScope = rememberCoroutineScope()
    var locationCountInput by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedLocations by remember { mutableStateOf(listOf<Location>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Generiraj lokacije", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = locationCountInput,
            onValueChange = { if (it.all(Char::isDigit)) locationCountInput = it },
            label = { Text("Koliko lokacij želite generirati?") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val count = locationCountInput.toIntOrNull() ?: 0
                if (count <= 0) {
                    statusMessage = "Vnesite veljavno pozitivno število."
                    return@Button
                }
                isGenerating = true
                statusMessage = null
                generatedLocations = emptyList()

                coroutineScope.launch {
                    var successCount = 0
                    var failedCount = 0
                    val newLocations = mutableListOf<Location>()

                    for (i in 1..count) {
                        val name = faker.address.city()
                        val identifier = name.uppercase(Locale.getDefault()) + Random.nextInt(100, 999)
                        val description = faker.string.toString()
                        val address = faker.address.fullAddress()
                        val (lat, lng) = generateRandomCoordinates()

                        val result = createLocation(
                            userId = userId,
                            name = name,
                            identifier = identifier,
                            description = description,
                            address = address,
                            lat = lat,
                            lng = lng
                        )

                        if (result.isSuccess) {
                            successCount++
                            newLocations.add(
                                Location(
                                    name = name,
                                    identifier = identifier,
                                    description = description,
                                    address = address,
                                    lat = lat,
                                    lng = lng,
                                    _id = ""
                                )
                            )
                        } else {
                            failedCount++
                        }
                    }

                    generatedLocations = newLocations
                    statusMessage = "Generacija zaključena. Uspešno: $successCount, Neuspešno: $failedCount"
                    isGenerating = false
                }
            },
            enabled = !isGenerating,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isGenerating) "Generiram..." else "Generiraj")
        }

        Spacer(modifier = Modifier.height(16.dp))

        statusMessage?.let {
            Text(text = it, style = MaterialTheme.typography.body1)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (generatedLocations.isNotEmpty()) {
            Text("Generirane lokacije:", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(generatedLocations) { location ->
                    LocationCard(location = location, onClick = {})
                }
            }
        }
    }
}

fun generateRandomCoordinates(): Pair<Double, Double> {
    val latitude = Random.nextDouble(45.42, 46.88)
    val longitude = Random.nextDouble(13.38, 16.60)
    return Pair(latitude, longitude)
}
