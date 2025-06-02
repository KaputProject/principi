package ui.pages

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Scraper() {
    var inputText1 by remember { mutableStateOf("") }
    var inputText2 by remember { mutableStateOf("") }
    var consoleOutput by remember { mutableStateOf("Čakam na podatke...") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = inputText1,
            onValueChange = { inputText1 = it },
            label = { Text("Vnesi besedilo 1") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { consoleOutput += "\n$inputText1" },
            modifier = Modifier.padding(top = 8.dp).align(Alignment.End)
        ) {
            Text("Izpiši 1")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputText2,
            onValueChange = { inputText2 = it },
            label = { Text("Vnesi besedilo 2") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { consoleOutput += "\n$inputText2" },
            modifier = Modifier.padding(top = 8.dp).align(Alignment.End)
        ) {
            Text("Izpiši 2")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = consoleOutput,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .border(1.dp, Color.Gray)
                .padding(8.dp),
            style = MaterialTheme.typography.body1
        )
    }
}
