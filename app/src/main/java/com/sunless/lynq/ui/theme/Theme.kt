package com.sunless.lynq.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Scheme = darkColorScheme(
    primary = TerminalPalette.ph,
    onPrimary = TerminalPalette.bg,
    background = TerminalPalette.bg,
    onBackground = TerminalPalette.ph,
    surface = TerminalPalette.panel,
    onSurface = TerminalPalette.ph,
    surfaceVariant = TerminalPalette.panelHi,
    onSurfaceVariant = TerminalPalette.phDim,
    outline = TerminalPalette.line,
    error = TerminalPalette.sos,
    onError = TerminalPalette.onSos,
)

/** ONE radius: 2dp, near-sharp, terminal-true. Used everywhere. */
private val LynqShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(2.dp),
    medium = RoundedCornerShape(2.dp),
    large = RoundedCornerShape(2.dp),
    extraLarge = RoundedCornerShape(2.dp),
)

@Composable
fun LynqTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLynqPalette provides TerminalPalette) {
        MaterialTheme(
            colorScheme = Scheme,
            typography = LynqTypography,
            shapes = LynqShapes,
            content = content,
        )
    }
}