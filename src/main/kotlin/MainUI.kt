import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFF6200EE)), // temno vijolična barva
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Moj Header",
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun Page1() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Številka strani: 1", style = MaterialTheme.typography.h4)
    }
}

@Composable
fun Page2() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Številka strani: 2", style = MaterialTheme.typography.h4)
    }
}

@Composable
fun Page3() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Številka strani: 3", style = MaterialTheme.typography.h4)
    }
}

@Composable
fun Page4() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Številka strani: 4", style = MaterialTheme.typography.h4)
    }
}

@Composable
fun App() {
    var currentPage by remember { mutableStateOf(1) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Header() // header na vrhu

            Row(modifier = Modifier.weight(1f)) {
                // Ukazna vrstica levo
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(150.dp)
                        .background(Color.LightGray),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = { currentPage = 1 }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text("Stran 1")
                    }
                    Button(onClick = { currentPage = 2 }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text("Stran 2")
                    }
                    Button(onClick = { currentPage = 3 }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text("Stran 3")
                    }
                    Button(onClick = { currentPage = 4 }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text("Stran 4")
                    }
                }

                // Glavni del kjer se prikazuje vsebina
                Box(modifier = Modifier.fillMaxSize()) {
                    when (currentPage) {
                        1 -> Page1()
                        2 -> Page2()
                        3 -> Page3()
                        4 -> Page4()
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Ukazna vrstica z gumbi in header") {
        App()
    }
}
