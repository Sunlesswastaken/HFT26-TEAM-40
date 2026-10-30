package com.sunless.lynq.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightScheme = lightColorScheme(
    primary = Color(0xFF141614),
    onPrimary = Color(0xFFFAFAF7),
    primaryContainer = Color(0xFFE7E7E0),
    onPrimaryContainer = Color(0xFF141614),
    secondary = Color(0xFF14634F),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF7F7F4),
    onBackground = Color(0xFF141614),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF141614),
    surfaceVariant = Color(0xFFEFEFE9),
    onSurfaceVariant = Color(0xFF5F645D),
    outline = Color(0xFFD5D5CC),
    outlineVariant = Color(0xFFE2E2DA),
    error = Color(0xFFB42318),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFCEDEB),
    onErrorContainer = Color(0xFF7A1A11),
)

private val DarkScheme = darkColorScheme(
    primary = Color(0xFFF1F3EF),
    onPrimary = Color(0xFF101210),
    primaryContainer = Color(0xFF222623),
    onPrimaryContainer = Color(0xFFECEEE9),
    secondary = Color(0xFF6FD3B2),
    onSecondary = Color(0xFF08251C),
    background = Color(0xFF0A0B0A),
    onBackground = Color(0xFFECEEE9),
    surface = Color(0xFF121413),
    onSurface = Color(0xFFECEEE9),
    surfaceVariant = Color(0xFF171A18),
    onSurfaceVariant = Color(0xFFA5ACA7),
    outline = Color(0xFF323734),
    outlineVariant = Color(0xFF262A27),
    error = Color(0xFFFF6A5D),
    onError = Color(0xFF2A0805),
    errorContainer = Color(0xFF2A1714),
    onErrorContainer = Color(0xFFFFDAD5),
)

private val LynqShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

@Composable
fun LynqTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalLynqPalette provides if (darkTheme) DarkPalette else LightPalette,
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkScheme else LightScheme,
            typography = LynqTypography,
            shapes = LynqShapes,
            content = content,
        )
    }
}
