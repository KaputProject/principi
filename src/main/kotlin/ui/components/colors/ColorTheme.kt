package ui.components.colors

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val BluePrimary = Color(0xFF1976D2)
val OrangeSecondary = Color(0xFFFFB300)
val BackgroundLight = Color(0xFFF5F5F5)
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)
fun getAppTypography(darkTheme: Boolean): Typography {
    val primaryText = if (darkTheme) Color(0xFFE0E0E0) else TextPrimary
    val secondaryText = if (darkTheme) Color(0xFFAAAAAA) else TextSecondary

    return Typography(
        h5 = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = primaryText
        ),
        body1 = TextStyle(
            fontSize = 16.sp,
            color = primaryText
        ),
        subtitle1 = TextStyle(
            fontSize = 14.sp,
            color = secondaryText
        )
    )
}


private val LightColorPalette = lightColors(
    primary = BluePrimary,
    primaryVariant = BluePrimary,
    secondary = OrangeSecondary,
    background = BackgroundLight,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

private val DarkColorPalette = darkColors(
    primary = BluePrimary,
    primaryVariant = BluePrimary,
    secondary = OrangeSecondary,
    background = Color(0xFF0D0D0D), // temnejše ozadje za boljši kontrast
    surface = Color(0xFF1A1A1A),     // rahlo svetlejša površina
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFFE0E0E0), // svetlo siva, ne čista bela (manj napora za oči)
    onSurface = Color(0xFFD6D6D6),    // prav tako svetlo siva, bolj naravno kot #FFFFFF
)


@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = getAppTypography(darkTheme),
        shapes = Shapes(),
        content = content
    )
}
