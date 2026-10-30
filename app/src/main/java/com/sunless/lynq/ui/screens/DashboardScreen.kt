package com.sunless.lynq.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.ui.components.AppShell
import com.sunless.lynq.ui.components.Eyebrow
import com.sunless.lynq.ui.components.LynqCard
import com.sunless.lynq.ui.components.MeshGraph
import com.sunless.lynq.ui.components.PeerAvatar
import com.sunless.lynq.ui.components.PriorityMark
import com.sunless.lynq.ui.components.RouteTrace
import com.sunless.lynq.ui.components.SectionHeader
import com.sunless.lynq.ui.components.SosButton
import com.sunless.lynq.ui.components.StatTile
import com.sunless.lynq.ui.components.StatusPill
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.util.asRelative

/**
 * Network Dashboard — the landing screen. Focal point: live mesh status
 * (hero numbers + topology), everything else recedes behind it.
 */
@Composable
fun DashboardScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
        ) {
            AppShell.TopBar(reachableCount = DemoData.reachableCount)

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Network\nDashboard",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 22.dp),
                )

                // Hero stats.
                Row(
                    modifier = Modifier.padding(top = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    StatTile(value = "${DemoData.reachableCount}", label = "Reachable", modifier = Modifier.weight(1f))
                    StatTile(value = "${DemoData.relayCount}", label = "Relaying", modifier = Modifier.weight(1f))
                    StatTile(value = "${DemoData.pendingMessages.size}", label = "Queued", modifier = Modifier.weight(1f))
                }

                MeshPreviewCard(navController, Modifier.padding(top = 32.dp))

                ActivitySection(Modifier.padding(top = 32.dp))

                SosButton(
                    onClick = { navController.navigate(Screen.Emergency.route) },
                    modifier = Modifier.padding(top = 32.dp),
                )
            }
        }

        AppShell.BottomBar(
            currentRoute = Screen.Dashboard.route,
            onSelect = { navController.navigate(it) },
        )
    }
}

@Composable
private fun MeshPreviewCard(navController: NavHostController, modifier: Modifier = Modifier) {
    LynqCard(modifier = modifier, contentPadding = 0.dp) {
        MeshGraph(peers = DemoData.peers, modifier = Modifier.padding(top = 8.dp))
        com.sunless.lynq.ui.components.Hairline()
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "${DemoData.relayCount} peers carrying traffic",
                style = MaterialTheme.typography.bodySmall,
                color = Lynq.palette.muted,
            )
            Text(
                text = "→",
                style = MaterialTheme.typography.bodyMedium,
                color = Lynq.palette.muted,
            )
        }
    }
}

@Composable
private fun ActivitySection(modifier: Modifier = Modifier) {
    Column(modifier) {
        SectionHeader(title = "Activity")
        LynqCard(contentPadding = 0.dp) {
            DemoData.activity.forEachIndexed { index, message ->
                if (index > 0) com.sunless.lynq.ui.components.Hairline()
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    PeerAvatar(name = message.senderName)
                    Column(modifier = Modifier.weight(1f)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(message.senderName, style = MaterialTheme.typography.titleMedium)
                            PriorityMark(priority = message.priority)
                        }
                        Text(
                            text = message.body,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        )
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        ) {
                            RouteTrace(route = message.routeHistory)
                            StatusPill(status = message.status)
                        }
                    }
                    Text(
                        text = message.createdAt.asRelative(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Lynq.palette.muted,
                    )
                }
            }
        }
    }
}