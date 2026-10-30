package com.sunless.lynq.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.DeviceHub
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.Lynq

/**
 * App shell: brand + live-status row on top, five-tab bar below.
 * The SOS tab is always in the SOS colour — the one accent the shell carries.
 */
object AppShell {

    private data class Tab(
        val label: String,
        val icon: ImageVector,
        val route: String,
        val isSos: Boolean = false,
    )

    private fun tabs() = listOf(
        Tab("Mesh", Icons.Outlined.DeviceHub, Screen.Dashboard.route),
        Tab("Messages", Icons.Outlined.ChatBubbleOutline, Screen.Messages.route),
        Tab("SOS", Icons.Filled.WarningAmber, Screen.Emergency.route, isSos = true),
        Tab("Explore", Icons.Outlined.Timeline, Screen.MeshExplorer.route),
        Tab("Settings", Icons.Outlined.Settings, Screen.Settings.route),
    )

    /** Brand + reachability pill, identical on every screen. */
    @Composable
    fun TopBar(reachableCount: Int, online: Boolean = true, modifier: Modifier = Modifier) {
        val palette = Lynq.palette
        val label = if (online) "$reachableCount reachable" else "Offline"
        val color = if (online) palette.signal else palette.muted
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Lynq",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            // Status is a dot *plus* text — never colour alone.
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .size(7.dp)
                        .background(color, CircleShape),
                )
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                )
            }
        }
    }

    @Composable
    fun BottomBar(
        currentRoute: String,
        modifier: Modifier = Modifier,
        onSelect: (String) -> Unit,
    ) {
        val palette = Lynq.palette
        val scheme = MaterialTheme.colorScheme
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = scheme.surface,
            border = BorderStroke(1.dp, palette.hairline),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                tabs().forEach { tab ->
                    val selected = tab.route == currentRoute
                    val tint = when {
                        tab.isSos -> palette.sos
                        selected -> scheme.onSurface
                        else -> palette.muted
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = tint,
                            modifier = Modifier.size(22.dp),
                        )
                        Text(
                            text = tab.label.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = tint,
                        )
                    }
                }
            }
        }
    }
}