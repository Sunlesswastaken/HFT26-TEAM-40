package com.sunless.lynq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.sunless.lynq.ui.components.CtOverlay
import com.sunless.lynq.ui.navigation.AppNavigation
import com.sunless.lynq.ui.theme.LynqTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dark is the design: edge-to-edge, transparent system bars
        enableEdgeToEdge()
        setContent {
            LynqTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                    CtOverlay()   // CRT scanlines over everything
                }
            }
        }
    }
}