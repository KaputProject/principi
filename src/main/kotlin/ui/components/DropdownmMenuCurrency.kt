import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import ui.enums.Currency

@Composable
fun DropdownMenuCurrency(
    selected: String,
    onSelect: (String) -> Unit
) {
    val currencies = Currency.entries.map { it.name }
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text("Valuta", fontSize = 12.sp) },
            textStyle = TextStyle(fontSize = 14.sp),
            readOnly = true,    // omogoči klik in da polje ni urejalno
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .height(56.dp),
            enabled = true,     // omogoči klik
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = MaterialTheme.colors.onSurface,
                disabledBorderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                disabledLabelColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)
            ),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                        contentDescription = "Toggle dropdown"
                    )
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(onClick = {
                    onSelect(currency)
                    expanded = false
                }) {
                    Text(currency, fontSize = 14.sp)
                }
            }
        }
    }
}

