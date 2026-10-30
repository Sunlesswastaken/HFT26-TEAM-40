package com.sunless.lynq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import com.sunless.lynq.ui.components.CtOverlay
import com.sunless.lynq.ui.navigation.AppNavigation
import com.sunless.lynq.ui.theme.LynqTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dark is the design: edge-to-edge with transparent system bars. The
        // CRT scanlines keep covering the full screen, but the app UI itself
        // is inset between status bar and gesture nav.
        enableEdgeToEdge()
        setContent {
            LynqTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    // App content inset away from status bar + gesture area.
                    AppNavigation(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.systemBars),
                    )
                    // CRT scanlines over EVERYTHING, full bleed.
                    CtOverlay()
                }
            }
        }
    }
}