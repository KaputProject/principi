package ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import classes.PdfParser
import classes.StatementParameters
import com.google.gson.GsonBuilder
import exchange.ExchangeRatesApi
import google.MapSearch
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun Scraper() {
    var user by remember { mutableStateOf("") }
    var partnersText by remember { mutableStateOf("") }
    var consoleOutput by remember { mutableStateOf("Čakam na podatke...") }
    var filePath by remember { mutableStateOf<String?>(null) }
    var openFileDialog by remember { mutableStateOf(false) }

    var mapQuery by remember { mutableStateOf("") }
    var exchangeBase by remember { mutableStateOf("EUR") }

    val coroutineScope = rememberCoroutineScope()
    val parser = remember { PdfParser() }
    val gson = remember { GsonBuilder().setPrettyPrinting().create() }

    val dotenv = dotenv {
        directory = "src/main/resources"
        ignoreIfMissing = true
    }

    val googleApiKey = dotenv["GOOGLE_API_KEY"]
        ?: System.getenv("GOOGLE_API_KEY")
        ?: error("GOOGLE_API_KEY is not set.")
    val mapSearch = remember { MapSearch(googleApiKey) }

    val exchangeRatesApi = remember { ExchangeRatesApi() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("PDF parser test", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        // PDF Parameters Card
        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Parametri PDF:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = user,
                    onValueChange = { user = it },
                    label = { Text("Uporabnik") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = partnersText,
                    onValueChange = { partnersText = it },
                    label = { Text("Partnerji (ločeni z vejico)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { openFileDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("Naloži PDF in razčleni", color = Color.White)
                }
            }
        }
        // Google Maps Card
        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Google Maps test", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = mapQuery,
                    onValueChange = { mapQuery = it },
                    label = { Text("Iskalni niz (npr. Restavracija Mango)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val results = withContext(Dispatchers.IO) { mapSearch.search(mapQuery) }
                            consoleOutput = gson.toJson(results)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("Išči", color = Color.White)
                }
            }
        }

        // Exchange Rates Card
        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Exchange Rates", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = exchangeBase,
                    onValueChange = { exchangeBase = it },
                    label = { Text("Valuta baze (npr. EUR, USD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val rates = withContext(Dispatchers.IO) {
                                exchangeRatesApi.get(exchangeBase.uppercase())
                            }
                            consoleOutput = gson.toJson(rates)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("Pridobi tečaje", color = Color.White)
                }
            }
        }

        // Rezultat Card
        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Rezultat:", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = consoleOutput,
                    onValueChange = { consoleOutput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    maxLines = 20,
                    textStyle = MaterialTheme.typography.body2,
                    readOnly = false,
                    enabled = true,
                )

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        user = ""
                        partnersText = ""
                        consoleOutput = "Čakam na podatke..."
                        filePath = null
                        mapQuery = ""
                        exchangeBase = "EUR"
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Počisti")
                }
            }
        }

        Spacer(Modifier.height(12.dp))
    }

    LaunchedEffect(openFileDialog) {
        if (openFileDialog) {
            openFileDialog = false
            FilePicker { file ->
                if (file != null) {
                    filePath = file.absolutePath
                    val partners = partnersText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    val parameters = StatementParameters(file = filePath!!, user = user, partners = partners)
                    val result = parser.parse(parameters)
                    consoleOutput = gson.toJson(result)
                } else {
                    consoleOutput += "\nNapaka pri izbiri PDF."
                }
            }
        }
    }
}

fun FilePicker(onFileSelected: (File?) -> Unit) {
    FileDialog(null as Frame?, "Izberi PDF", FileDialog.LOAD).apply {
        isVisible = true
        val selected = file?.let { File(directory, it) }
        onFileSelected(selected)
    }
}
