package com.sunless.lynq.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Semantic colours that Material's [androidx.compose.material3.ColorScheme] has no slot
 * for. Kept deliberately small: every screen should read from here or from
 * [androidx.compose.material3.MaterialTheme.colorScheme], never from raw hex values.
 */
@Immutable
data class LynqPalette(
    val sos: Color,
    val onSos: Color,
    val sosSoft: Color,
    val signal: Color,
    val warn: Color,
    val hairline: Color,
    val muted: Color,
    val sunken: Color,
)

val LightPalette = LynqPalette(
    sos = Color(0xFFB42318),
    onSos = Color(0xFFFFFFFF),
    sosSoft = Color(0xFFFCEDEB),
    signal = Color(0xFF14634F),
    warn = Color(0xFF8A5A00),
    hairline = Color(0xFFE2E2DA),
    muted = Color(0xFF6B7069),
    sunken = Color(0xFFEFEFE9),
)

val DarkPalette = LynqPalette(
    sos = Color(0xFFFF6A5D),
    onSos = Color(0xFF2A0805),
    sosSoft = Color(0xFF2A1714),
    signal = Color(0xFF6FD3B2),
    warn = Color(0xFFE0A94D),
    hairline = Color(0xFF262A27),
    muted = Color(0xFF98A09B),
    sunken = Color(0xFF151816),
)

val LocalLynqPalette = staticCompositionLocalOf { LightPalette }

/** `Lynq.palette.sos`, `Lynq.palette.hairline`, ... */
object Lynq {
    val palette: LynqPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalLynqPalette.current
}
