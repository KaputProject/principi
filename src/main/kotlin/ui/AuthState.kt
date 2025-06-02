package ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AuthState {
    var token by mutableStateOf<String?>(null)
    var username by mutableStateOf<String?>(null)
}
