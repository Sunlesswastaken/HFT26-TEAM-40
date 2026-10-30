package com.sunless.lynq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import com.sunless.lynq.ui.navigation.AppNavigation
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.ui.theme.LynqTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LynqTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Lynq.pal.bg)
                        .windowInsetsPadding(WindowInsets.systemBars),
                ) {
                    AppNavigation(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}