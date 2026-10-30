package com.sunless.lynq.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sunless.lynq.R

/**
 * Three-tier type system:
 * - Silkscreen (pixel bold) = identity: titles, buttons, tags, readouts, names
 * - JetBrains Mono = reading + data: bodies, IDs, timestamps, hops
 * - VT323 = CRT flavor only: boot log, ASCII topology art
 *
 * Sizing note: Silkscreen and VT323 fill their em box much more than Roboto,
 * so their sp values run lower than typical Material sizes. Values below were
 * checked on a real device (Samsung A069, ~410dpi).
 */
val PixelFont = FontFamily(
    Font(R.font.silkscreen_bold, FontWeight.Bold),
)

val MonoFont = FontFamily(
    Font(R.font.jetbrainsmono_regular, FontWeight.Normal),
    Font(R.font.jetbrainsmono_bold, FontWeight.Bold),
)

val TerminalFont = FontFamily(
    Font(R.font.vt323_regular, FontWeight.Normal),
)

val LynqTypography = Typography(
    // ---- pixel tier: identity ----
    displaySmall = TextStyle(          // big readouts
        fontFamily = PixelFont,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,              // pixel fonts run "big" — keep modest
        lineHeight = 28.sp,
    ),
    headlineMedium = TextStyle(        // screen titles
        fontFamily = PixelFont,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    titleMedium = TextStyle(           // names — people are headings, stay pixel
        fontFamily = PixelFont,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelSmall = TextStyle(            // tags, chips, section heads
        fontFamily = PixelFont,
        fontWeight = FontWeight.Bold,
        fontSize = 8.5.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.8.sp,
    ),
    // ---- mono tier: reading + data ----
    bodyLarge = TextStyle(              // message bodies
        fontFamily = MonoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 19.sp,
    ),
    bodyMedium = TextStyle(             // previews, notes
        fontFamily = MonoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 17.sp,
    ),
    bodySmall = TextStyle(              // readable meta
        fontFamily = MonoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 15.sp,
    ),
    labelLarge = TextStyle(             // mono buttons / meter
        fontFamily = MonoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelMedium = TextStyle(            // ids, timestamps, hop counts
        fontFamily = MonoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 10.5.sp,
        lineHeight = 14.sp,
    ),
)

/** VT323 CRT flavor: boot log lines, ASCII art. */
val TerminalStyle = TextStyle(
    fontFamily = TerminalFont,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 19.sp,
)

/** Pixel route trace: YOU > AARAV > KABIR */
val RouteStyle = TextStyle(
    fontFamily = PixelFont,
    fontWeight = FontWeight.Bold,
    fontSize = 9.sp,
    lineHeight = 13.sp,
    letterSpacing = 0.3.sp,
)