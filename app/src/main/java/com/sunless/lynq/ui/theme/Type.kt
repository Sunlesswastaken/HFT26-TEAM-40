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
    displaySmall = TextStyle(          // big readouts (34px pixel)
        fontFamily = PixelFont,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 34.sp,
    ),
    headlineMedium = TextStyle(        // screen titles
        fontFamily = PixelFont,
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp,
    ),
    titleMedium = TextStyle(           // names — people are headings, stay pixel
        fontFamily = PixelFont,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(             // tags, status chips, section heads
        fontFamily = PixelFont,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.sp,
    ),
    // ---- mono tier: reading + data ----
    bodyLarge = TextStyle(              // message bodies
        fontFamily = MonoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
    ),
    bodyMedium = TextStyle(             // previews, notes, descriptions
        fontFamily = MonoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 13.5.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(              // readable meta
        fontFamily = MonoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.5.sp,
        lineHeight = 17.sp,
    ),
    labelLarge = TextStyle(             // buttons already pixel via .pix styles; this is mono buttons
        fontFamily = MonoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    ),
    labelMedium = TextStyle(            // ids, timestamps, hop counts
        fontFamily = MonoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)

/** VT323 CRT flavor: boot log lines, ASCII art. */
val TerminalStyle = TextStyle(
    fontFamily = TerminalFont,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
)

/** Pixel route trace: YOU > AARAV > KABIR */
val RouteStyle = TextStyle(
    fontFamily = PixelFont,
    fontWeight = FontWeight.Bold,
    fontSize = 10.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.5.sp,
)