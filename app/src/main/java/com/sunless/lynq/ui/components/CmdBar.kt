package com.sunless.lynq.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.clip
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
 * Apple Floating Rounded Island Navigation Bar (Capsule Dock).
 *
 * Clean detached pill dock:
 * - Rounded capsule shape (32dp continuous curvature)
 * - Translucent Apple secondary dark surface with subtle hairline border
 * - Smooth animated pill highlight for active tab
 * - Dedicated high-visibility SOS emergency pill
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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = Color(0xF41C1C1E), // Frosted Apple Secondary Background (95% opaque)
            border = BorderStroke(0.5.dp, Color(0x33FFFFFF)), // Apple delicate hairline
            shadowElevation = 14.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
                        label = "tabColor",
                    )

                    val pillBg by animateColorAsState(
                        targetValue = when {
                            item.isEmergency && active -> pal.emergencyRed.copy(alpha = 0.22f)
                            active -> pal.accentBlue.copy(alpha = 0.16f)
                            else -> Color.Transparent
                        },
                        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
                        label = "pillBg",
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(pillBg)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { onSelect(item.route) },
                            )
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = itemColor,
                                modifier = Modifier.size(21.dp),
                            )
                            Text(
                                text = item.label,
                                color = itemColor,
                                style = AppleCaption,
                                fontSize = 10.sp,
                                fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                                maxLines = 1,
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}