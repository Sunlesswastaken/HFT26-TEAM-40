package com.sunless.lynq.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.ui.components.AppShell
import com.sunless.lynq.ui.components.Hairline
import com.sunless.lynq.ui.components.LynqCard
import com.sunless.lynq.ui.components.SectionHeader
import com.sunless.lynq.ui.components.StatusPill
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.ui.theme.MonoStyle

/**
 * Settings & Identity — who this device is, what it promises, and what it
 * is allowed to do. Ends with the honest scope note.
 */
@Composable
fun SettingsScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp),
        ) {
            AppShell.TopBar(reachableCount = DemoData.reachableCount)

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 22.dp),
                )

                IdentityCard(Modifier.padding(top = 32.dp))

                SettingsGroup(
                    title = "Security",
                    modifier = Modifier.padding(top = 32.dp),
                ) {
                    SettingRow(
                        title = "End-to-end encryption",
                        sub = "AES-256-GCM · keys never leave this device",
                        trailing = { StatusPill(status = com.sunless.lynq.domain.model.DeliveryStatus.DELIVERED, labelOverride = "On", positive = true) },
                    )
                    Hairline()
                    SettingRow(
                        title = "Signed identity",
                        sub = "Every outgoing message is signed",
                        trailing = { StatusPill(status = com.sunless.lynq.domain.model.DeliveryStatus.DELIVERED, labelOverride = "On", positive = true) },
                    )
                }

                KeyValueGroup(
                    title = "Battery & network",
                    modifier = Modifier.padding(top = 32.dp),
                    rows = listOf(
                        "Relay for others" to "Always",
                        "Discovery" to "Continuous",
                        "Message retention" to "6 hours",
                    ),
                )

                SettingsGroup(
                    title = "Permissions",
                    modifier = Modifier.padding(top = 32.dp),
                ) {
                    SettingRow(
                        title = "Nearby devices",
                        sub = "Find and talk to Lynq devices around you",
                        trailing = { StatusPill(status = com.sunless.lynq.domain.model.DeliveryStatus.DELIVERED, labelOverride = "Granted", positive = true) },
                    )
                    Hairline()
                    SettingRow(
                        title = "Location",
                        sub = "Only used when you attach it to an SOS",
                        trailing = { StatusPill(status = com.sunless.lynq.domain.model.DeliveryStatus.SENT, labelOverride = "Not set", positive = false) },
                    )
                    Hairline()
                    SettingRow(
                        title = "Notifications",
                        sub = "Alerts you when an SOS arrives",
                        trailing = { StatusPill(status = com.sunless.lynq.domain.model.DeliveryStatus.DELIVERED, labelOverride = "Granted", positive = true) },
                    )
                }

                Text(
                    text = "Lynq relays messages between nearby devices only. It cannot reach emergency services without a cellular or internet path.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Lynq.palette.muted,
                    modifier = Modifier.padding(top = 32.dp),
                )
            }
        }

        AppShell.BottomBar(
            currentRoute = Screen.Settings.route,
            onSelect = { navController.navigate(it) },
        )
    }
}

@Composable
private fun IdentityCard(modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    LynqCard(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = scheme.onSurface,
                contentColor = scheme.surface,
            ) {
                androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = DemoData.MY_NAME.take(1),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(DemoData.MY_NAME, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = DemoData.MY_ID,
                    style = MonoStyle,
                    color = Lynq.palette.muted,
                    modifier = Modifier.padding(top = 3.dp),
                )
                Text(
                    text = DemoData.MY_DEVICE,
                    style = MaterialTheme.typography.bodySmall,
                    color = Lynq.palette.muted,
                )
            }
        }
    }
    Text(
        text = "Your ID is the fingerprint of this device's key. Peers verify every message you sign with it.",
        style = MaterialTheme.typography.bodySmall,
        color = Lynq.palette.muted,
        modifier = Modifier.padding(top = 12.dp),
    )
}

@Composable
private fun SettingsGroup(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier) {
        SectionHeader(title = title)
        LynqCard(modifier = Modifier.padding(top = 12.dp), contentPadding = 0.dp, content = { content() })
    }
}

@Composable
private fun SettingRow(
    title: String,
    sub: String,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = sub,
                style = MaterialTheme.typography.bodySmall,
                color = Lynq.palette.muted,
            )
        }
        trailing?.invoke()
    }
}

@Composable
private fun KeyValueGroup(
    title: String,
    modifier: Modifier = Modifier,
    rows: List<Pair<String, String>>,
) {
    Column(modifier) {
        SectionHeader(title = title)
        LynqCard(modifier = Modifier.padding(top = 12.dp)) {
            rows.forEachIndexed { index, (k, v) ->
                if (index > 0) Hairline()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 11.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(k, style = MaterialTheme.typography.bodyMedium, color = Lynq.palette.muted)
                    Text(v, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}