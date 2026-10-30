package com.sunless.lynq.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Apple Minimal Dark System Palette for Lynq.
 * Built following Apple iOS Human Interface Guidelines (Dark Mode)
 * and Oil-UI principles: Pure OLED black, subtle grouped surfaces,
 * Apple System Accents, and zero gratuitous neon borders.
 */
@Immutable
data class LynqPalette(
    // Base surfaces (Apple Dark System Backgrounds)
    val bg: Color,               // Pure OLED Black #000000
    val surface: Color,          // Secondary System Background #1C1C1E
    val surfaceElevated: Color,  // Tertiary System Background #2C2C2E
    val surfaceMuted: Color,     // Quaternary System Background #3A3A3C
    val border: Color,           // Hairline subtle separator (rgba(255,255,255,0.08))
    val borderSubtle: Color,     // Very faint separator (rgba(255,255,255,0.04))
    val borderActive: Color,     // Active selection border (Apple Blue)

    // High-contrast text tiers (Apple SF Dark Mode)
    val textPrimary: Color,      // #FFFFFF (100% white)
    val textSecondary: Color,    // #8E8E93 (Apple System Gray)
    val textMuted: Color,        // #636366 (Apple System Gray 2)

    // Apple System Accents
    val accentCyan: Color,       // Primary Apple Cyan #32ADE6 / #00E5FF
    val accentCyanGlow: Color,   // Soft ambient glow
    val accentBlue: Color,       // Apple System Blue #0A84FF
    val accentGreen: Color,      // Apple System Green #30D158 (Online/Delivered)
    val warningAmber: Color,     // Apple System Orange #FF9F0A (Pending/Hop)
    val emergencyRed: Color,     // Apple System Red #FF453A (SOS Emergency)
    val emergencyRedGlow: Color, // Soft red aura for SOS
    val onEmergency: Color,      // Text on emergency red (#FFFFFF)
)

val DarkPalette = LynqPalette(
    bg = Color(0xFF000000),             // Pure Apple OLED Black
    surface = Color(0xFF1C1C1E),        // Apple Secondary System Background
    surfaceElevated = Color(0xFF2C2C2E),// Apple Tertiary System Background
    surfaceMuted = Color(0xFF3A3A3C),   // Apple Quaternary Fill
    border = Color(0x1FFFFFFF),         // Hairline 12% white separator
    borderSubtle = Color(0x0FFFFFFF),   // Faint 6% white divider
    borderActive = Color(0xFF0A84FF),   // Apple System Blue

    textPrimary = Color(0xFFFFFFFF),    // Crisp White
    textSecondary = Color(0xFF8E8E93),  // iOS System Gray
    textMuted = Color(0xFF636366),      // iOS System Gray 2

    accentCyan = Color(0xFF32ADE6),     // Apple System Cyan
    accentCyanGlow = Color(0x2632ADE6),
    accentBlue = Color(0xFF0A84FF),     // Apple System Blue
    accentGreen = Color(0xFF30D158),    // Apple System Green
    warningAmber = Color(0xFFFF9F0A),   // Apple System Orange
    emergencyRed = Color(0xFFFF453A),   // Apple System Red
    emergencyRedGlow = Color(0x33FF453A),
    onEmergency = Color(0xFFFFFFFF),
)

val LocalLynqPalette = staticCompositionLocalOf { DarkPalette }

/** Easy accessor: `Lynq.pal.accentCyan`, `Lynq.pal.surface`, etc. */
object Lynq {
    val pal: LynqPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalLynqPalette.current
}