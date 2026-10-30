package com.sunless.lynq.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

private val Scheme = darkColorScheme(
    primary = DarkPalette.accentCyan,
    onPrimary = DarkPalette.bg,
    primaryContainer = DarkPalette.surfaceElevated,
    onPrimaryContainer = DarkPalette.textPrimary,
    secondary = DarkPalette.accentBlue,
    onSecondary = DarkPalette.bg,
    background = DarkPalette.bg,
    onBackground = DarkPalette.textPrimary,
    surface = DarkPalette.surface,
    onSurface = DarkPalette.textPrimary,
    surfaceVariant = DarkPalette.surfaceMuted,
    onSurfaceVariant = DarkPalette.textSecondary,
    outline = DarkPalette.border,
    outlineVariant = DarkPalette.borderSubtle,
    error = DarkPalette.emergencyRed,
    onError = DarkPalette.onEmergency,
)

/** Modern refined corner curves. */
private val ModernShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(30.dp),
)

@Composable
fun LynqTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLynqPalette provides DarkPalette) {
        MaterialTheme(
            colorScheme = Scheme,
            typography = LynqTypography,
            shapes = ModernShapes,
            content = content,
        )
    }
}