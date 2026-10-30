package com.sunless.lynq.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Field-terminal palette. Dark IS the design — there is no light mode.
 * One accent (phosphor green); amber is warnings only; red is SOS only.
 */
@Immutable
data class LynqPalette(
    // surfaces
    val bg: Color,        // page — near-black, green-tinted, never #000
    val panel: Color,     // panels, rows, nav
    val panelHi: Color,   // raised panels, active inputs
    val line: Color,      // hairline borders
    val lineHi: Color,    // lit borders (focus/accent frames)
    // text
    val ph: Color,        // phosphor green: primary text
    val phDim: Color,     // secondary
    val phFaint: Color,   // meta, timestamps
    // accents (strict meanings)
    val warn: Color,      // amber: warnings + HIGH priority only
    val sos: Color,       // red: SOS / critical only
    val onSos: Color,
)

val TerminalPalette = LynqPalette(
    bg = Color(0xFF060A07),
    panel = Color(0xFF0A120C),
    panelHi = Color(0xFF0E1A10),
    line = Color(0xFF1C3320),
    lineHi = Color(0xFF2E5236),
    ph = Color(0xFF7DF2A0),
    phDim = Color(0xFF5FC47F),
    phFaint = Color(0xFF4E9C69),
    warn = Color(0xFFE8C468),
    sos = Color(0xFFFF7A68),
    onSos = Color(0xFF140504),
)

val LocalLynqPalette = staticCompositionLocalOf { TerminalPalette }

/** `Lynq.pal.ph`, `Lynq.pal.line`, ... */
object Lynq {
    val pal: LynqPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalLynqPalette.current
}