import Database.DatabaseUtil
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.bson.Document
import java.time.LocalDate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid


@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFFE6F2F3)), // svetla pastelna modra
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Kaput admin panel",
            color = Color(0xFF00796B),
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
    }
}


@Composable
fun Page1() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("2025-05-27") }
    var surname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .width(400.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Vnesi podatke uporabnika", style = MaterialTheme.typography.h5)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = { Text("Date of Birth (yyyy-MM-dd)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Surname") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        try {
                            val datum = LocalDate.parse(dateOfBirth)
                            val user = Document()
                                .append("name", name)
                                .append("email", email)
                                .append("dateOfBirth", datum)
                                .append("surname", surname)
                                .append("password", password)
                                .append("username", username)

                            val collection = DatabaseUtil().database.getCollection("users")
                            collection.insertOne(user)

                            message = "Uporabnik uspešno dodan!"
                        } catch (e: Exception) {
                            message = "Napaka: ${e.message}"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFB2DFDB)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.elevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Shrani uporabnika", color = Color(0xFF004D40))
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (message.isNotEmpty()) {
                    Text(
                        text = message,
                        color = if (message.startsWith("Napaka")) Color.Red else Color.Green
                    )
                }
            }
        }
    }
}

@Composable
fun SidebarButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE6F2F3)),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevation(4.dp)
    ) {
        Text(label, color = Color(0xFF004D40))
    }
}



@Composable
fun Page2() {
    val people = listOf(
        "Daysi" to "ZBONCAK",
        "Brady" to "BERGNAUM",
        "Simon" to "WEST",
        "Wendell" to "SCHILLER"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("People", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 12.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // popravljen parameter
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(people.size) { index ->
                val (name, surname) = people[index]
                PersonCard(name, surname)
            }
        }

    }
}

@Composable
fun PersonCard(name: String, surname: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        backgroundColor = Color(0xFFE6F2F3),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = Color(0xFF00796B)
            )
            Text(name, style = MaterialTheme.typography.subtitle1)
            Text(surname, style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
        }
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
        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
            Header()

            Row(modifier = Modifier.weight(1f)) {
                // Sidebar
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(180.dp)
                        .background(Color(0xFFF2F2F2)),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    SidebarButton("Add person") { currentPage = 1 }
                    SidebarButton("People") { currentPage = 2 }
                    SidebarButton("Scraper") { currentPage = 3 }
                    SidebarButton("Generator") { currentPage = 4 }
                    Spacer(modifier = Modifier.weight(1f))
                    SidebarButton("About") { /* lahko dodaš Page5 */ }
                }

                // Main content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(8.dp)
                ) {
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
    Window(onCloseRequest = ::exitApplication, title = "Compose Database Admin") {
        App()
    }
}
