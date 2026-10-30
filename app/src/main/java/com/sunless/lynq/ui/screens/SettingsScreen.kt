package com.sunless.lynq.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Emergency
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.ui.components.AppleDivider
import com.sunless.lynq.ui.components.AppleGroupedCard
import com.sunless.lynq.ui.components.CmdBar
import com.sunless.lynq.ui.components.SectionHeader
import com.sunless.lynq.ui.components.TopBar
import com.sunless.lynq.ui.components.UserAvatar
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.navigation.navigateTab
import com.sunless.lynq.ui.theme.AppleBody
import com.sunless.lynq.ui.theme.AppleCaption
import com.sunless.lynq.ui.theme.AppleFootnote
import com.sunless.lynq.ui.theme.AppleHeadline
import com.sunless.lynq.ui.theme.AppleSubhead
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.ui.theme.TechCodeStyle

@Composable
fun SettingsScreen(navController: NavHostController) {
    val pal = Lynq.pal
    var autoRelay by remember { mutableStateOf(true) }
    var locationSharing by remember { mutableStateOf(true) }
    var highPowerScan by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
        ) {
            TopBar(reachableCount = DemoData.reachableCount, title = "Settings")

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Profile Section
                AppleGroupedCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        UserAvatar(name = DemoData.MY_NAME, size = 52.dp, isRelay = true)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = DemoData.MY_NAME,
                                style = AppleHeadline,
                                fontWeight = FontWeight.Bold,
                                color = pal.textPrimary,
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "${DemoData.MY_DEVICE} · Keystore Verified",
                                style = AppleFootnote,
                                color = pal.accentGreen,
                            )
                        }
                    }

                    AppleDivider()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            Text(
                                text = "Device Fingerprint",
                                style = AppleFootnote,
                                color = pal.textSecondary,
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "9f3c • a7e5 • 4b02 • c1aa",
                                style = TechCodeStyle,
                                color = pal.textPrimary,
                                fontSize = 12.sp,
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                            tint = pal.textMuted,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }

                Spacer(Modifier.height(22.dp))

                // Radio & Mesh Connectivity
                SectionHeader(title = "Radio & Mesh")
                Spacer(Modifier.height(10.dp))

                AppleGroupedCard {
                    SettingsSwitchRow(
                        icon = Icons.Rounded.Wifi,
                        iconBg = pal.accentBlue,
                        title = "High-Power Wi-Fi Discovery",
                        subtitle = "Strategy.P2P_CLUSTER active",
                        checked = highPowerScan,
                        onCheckedChange = { highPowerScan = it },
                    )
                    AppleDivider()
                    SettingsSwitchRow(
                        icon = Icons.Rounded.Hub,
                        iconBg = pal.accentGreen,
                        title = "Automatic SOS Relaying",
                        subtitle = "Hop-by-hop mesh forwarding",
                        checked = autoRelay,
                        onCheckedChange = { autoRelay = it },
                    )
                    AppleDivider()
                    SettingsValueRow(
                        icon = Icons.Rounded.Bluetooth,
                        iconBg = Color(0xFF5E5CE6),
                        title = "Hop Limit Budget (TTL)",
                        value = "6 hops",
                    )
                }

                Spacer(Modifier.height(22.dp))

                // Emergency & Security
                SectionHeader(title = "Security & Privacy")
                Spacer(Modifier.height(10.dp))

                AppleGroupedCard {
                    SettingsValueRow(
                        icon = Icons.Rounded.Lock,
                        iconBg = pal.accentGreen,
                        title = "Chat Payload Encryption",
                        value = "AES-256-GCM",
                    )
                    AppleDivider()
                    SettingsValueRow(
                        icon = Icons.Rounded.Fingerprint,
                        iconBg = pal.accentBlue,
                        title = "Hardware Keystore",
                        value = "ECDSA P-256",
                    )
                    AppleDivider()
                    SettingsSwitchRow(
                        icon = Icons.Rounded.Emergency,
                        iconBg = pal.emergencyRed,
                        title = "Emergency Location Broadcast",
                        subtitle = "Include GPS in SOS alerts",
                        checked = locationSharing,
                        onCheckedChange = { locationSharing = it },
                    )
                }

                Spacer(Modifier.height(22.dp))

                // About
                SectionHeader(title = "About")
                Spacer(Modifier.height(10.dp))

                AppleGroupedCard {
                    SettingsValueRow(
                        icon = Icons.Rounded.Security,
                        iconBg = Color(0xFF636366),
                        title = "Protocol Version",
                        value = "Lynq v1.0",
                    )
                    AppleDivider()
                    SettingsValueRow(
                        icon = Icons.Rounded.CheckCircle,
                        iconBg = Color(0xFF30D158),
                        title = "Local Database",
                        value = "Room SQLite Encrypted",
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Lynq Emergency Mesh · 100% Offline · No Cellular Needed",
                    style = AppleCaption,
                    color = pal.textMuted,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }

        CmdBar(
            currentRoute = Screen.Settings.route,
            onSelect = { navController.navigateTab(it) },
        )
    }
}

@Composable
private fun SettingsSwitchRow(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val pal = Lynq.pal
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(iconBg, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                )
            }
            Column {
                Text(
                    text = title,
                    style = AppleHeadline,
                    color = pal.textPrimary,
                    fontSize = 15.sp,
                )
                Text(
                    text = subtitle,
                    style = AppleCaption,
                    color = pal.textSecondary,
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = pal.accentGreen,
                uncheckedThumbColor = pal.textMuted,
                uncheckedTrackColor = pal.surfaceElevated,
            ),
        )
    }
}

@Composable
private fun SettingsValueRow(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    value: String,
) {
    val pal = Lynq.pal
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(iconBg, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                )
            }
            Text(
                text = title,
                style = AppleHeadline,
                color = pal.textPrimary,
                fontSize = 15.sp,
            )
        }

        Text(
            text = value,
            style = AppleFootnote,
            color = pal.textSecondary,
        )
    }
}