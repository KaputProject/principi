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
val AppTypography = Typography(
    h5 = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
    ),
    body1 = TextStyle(
        fontSize = 16.sp,
        color = TextPrimary
    ),
    subtitle1 = TextStyle(
        fontSize = 14.sp,
        color = TextSecondary
    )
)

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
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = AppTypography,  // tukaj uporabi svoj typography
        shapes = Shapes(),            // lahko definiraš svoje ali uporabiš default
        content = content
    )
}
