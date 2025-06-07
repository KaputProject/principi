package ui.pages

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.AwtWindow
import classes.PdfParser
import classes.StatementParameters
import com.google.gson.GsonBuilder
import google.MapSearch
import google.Place
import io.github.cdimascio.dotenv.dotenv
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Scraper() {
    var user by remember { mutableStateOf("") }
    var partnersText by remember { mutableStateOf("") }
    var consoleOutput by remember { mutableStateOf("Čakam na podatke...") }
    var filePath by remember { mutableStateOf<String?>(null) }
    var openFileDialog by remember { mutableStateOf(false) }

    val parser = remember { PdfParser() }

    var mapQuery by remember { mutableStateOf("") }
    var places by remember { mutableStateOf<List<Place>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val dotenv = dotenv {
        directory = "src/main/resources"
        ignoreIfMissing = true
    }
    val apiKey = dotenv["GOOGLE_API_KEY"] ?: System.getenv("GOOGLE_API_KEY") ?: error("API ključ ni nastavljen.")

    val coroutineScope = rememberCoroutineScope()
    val mapSearch = remember { MapSearch(apiKey) }
    val gson = GsonBuilder().setPrettyPrinting().create()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("PDF parser test", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Parametri PDF:", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = user,
                    onValueChange = { user = it },
                    label = { Text("Uporabnik") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = partnersText,
                    onValueChange = { partnersText = it },
                    label = { Text("Partnerji (ločeni z vejico)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = { openFileDialog = true },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Naloži PDF in razčleni")
        }

        Spacer(Modifier.height(16.dp))

        Text("Google Maps test", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = mapQuery,
            onValueChange = { mapQuery = it },
            label = { Text("Iskalni niz (npr. Restavracija Mango)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        mapSearch.search(mapQuery)
                    }

                    consoleOutput = gson.toJson(result)
                }
            }
        ) {
            Text("Išči")
        }

        Spacer(Modifier.height(8.dp))

        Text("Rezultat:", fontWeight = FontWeight.SemiBold)

        OutlinedTextField(
            value = consoleOutput,
            onValueChange = { consoleOutput = it },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp),
            maxLines = 20,
            textStyle = MaterialTheme.typography.body2
        )

        Spacer(Modifier.height(12.dp))
    }

    if (openFileDialog) {
        FilePicker { file ->
            openFileDialog = false
            if (file != null) {
                filePath = file.absolutePath

                val partners = partnersText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                val parameters = StatementParameters(file = filePath!!, user = user, partners = partners)

                val result = parser.parse(parameters)

                consoleOutput = gson.toJson(result)
            } else {
                consoleOutput += "\nIzbira PDF datoteke je bila preklicana."
            }
        }
    }
}

@Composable
fun FilePicker(onFileSelected: (File?) -> Unit) {
    AwtWindow(
        create = {
            object : FileDialog(null as Frame?, "Izberi PDF", FileDialog.LOAD) {
                init {
                    isVisible = true
                    val selected = file?.let { File(directory, it) }
                    onFileSelected(selected)
                }
            }
        },
        dispose = {}
    )
}
