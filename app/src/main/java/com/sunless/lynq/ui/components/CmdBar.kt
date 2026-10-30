package com.sunless.lynq.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Emergency
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Sensors
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.AppleCaption
import com.sunless.lynq.ui.theme.Lynq

private data class NavItemSpec(
    val label: String,
    val icon: ImageVector,
    val route: String,
    val isEmergency: Boolean = false,
)

/**
 * Apple Minimalist Bottom Navigation Bar.
 * Clean iOS tab bar styling with frosted background, subtle hairline border,
 * SF-style vector glyphs, and high-visibility SOS tab.
 */
@Composable
fun CmdBar(
    currentRoute: String,
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit,
) {
    val pal = Lynq.pal
    val items = listOf(
        NavItemSpec("Network", Icons.Rounded.Sensors, Screen.Dashboard.route),
        NavItemSpec("Messages", Icons.Rounded.ChatBubbleOutline, Screen.Messages.route),
        NavItemSpec("SOS", Icons.Rounded.Emergency, Screen.Emergency.route, isEmergency = true),
        NavItemSpec("Mesh", Icons.Rounded.Hub, Screen.MeshExplorer.route),
        NavItemSpec("Settings", Icons.Rounded.Settings, Screen.Settings.route),
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF121214),
        border = BorderStroke(0.5.dp, pal.border),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                val active = item.route == currentRoute
                val interactionSource = remember { MutableInteractionSource() }

                val itemColor by animateColorAsState(
                    targetValue = when {
                        item.isEmergency && active -> pal.emergencyRed
                        item.isEmergency -> pal.emergencyRed.copy(alpha = 0.85f)
                        active -> pal.accentBlue
                        else -> pal.textSecondary
                    },
                    label = "tabColor",
                )

                Column(
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onSelect(item.route) },
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (item.isEmergency) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    if (active) pal.emergencyRed.copy(alpha = 0.2f) else Color.Transparent,
                                    CircleShape,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = itemColor,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = itemColor,
                            modifier = Modifier.size(22.dp),
                        )
                    }

                    Text(
                        text = item.label,
                        color = itemColor,
                        style = AppleCaption,
                        fontSize = 10.sp,
                        fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
            }
        }
    }
}