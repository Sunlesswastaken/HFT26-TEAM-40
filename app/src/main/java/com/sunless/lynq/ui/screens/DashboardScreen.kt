package com.sunless.lynq.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Sensors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.domain.model.DeliveryStatus
import com.sunless.lynq.domain.model.Priority
import com.sunless.lynq.ui.components.AppleDivider
import com.sunless.lynq.ui.components.AppleGroupedCard
import com.sunless.lynq.ui.components.CmdBar
import com.sunless.lynq.ui.components.MeshGraph
import com.sunless.lynq.ui.components.MetricCard
import com.sunless.lynq.ui.components.ModernRouteTrace
import com.sunless.lynq.ui.components.PriorityBadge
import com.sunless.lynq.ui.components.SectionHeader
import com.sunless.lynq.ui.components.SosLever
import com.sunless.lynq.ui.components.StatusChip
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
import com.sunless.lynq.util.asRelative

@Composable
fun DashboardScreen(navController: NavHostController) {
    val pal = Lynq.pal

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
        ) {
            TopBar(reachableCount = DemoData.reachableCount, title = "Network")

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Headline Status
                Text(
                    text = "Decentralized mesh active · 0 cellular needed",
                    style = AppleSubhead,
                    color = pal.textSecondary,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                // Protagonist: Live Obsidian Mesh Graph
                MeshGraph(
                    peers = DemoData.peers,
                    liveRoute = listOf("You", "Aarav", "Kabir", "Priya"),
                    height = 230.dp,
                    interactive = true,
                )

                Spacer(Modifier.height(16.dp))

                // Apple Emergency SOS Trigger
                SosLever(
                    onClick = { navController.navigateTab(Screen.Emergency.route) },
                )

                Spacer(Modifier.height(16.dp))

                // Clean Apple Metric Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    MetricCard(
                        value = "${DemoData.reachableCount}",
                        label = "Reachable",
                        accentColor = pal.accentBlue,
                        icon = Icons.Rounded.Sensors,
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        value = "${DemoData.relayCount}",
                        label = "Relaying",
                        accentColor = pal.accentGreen,
                        icon = Icons.Rounded.Hub,
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        value = "${DemoData.pendingMessages.size}",
                        label = "Queued",
                        accentColor = pal.warningAmber,
                        icon = Icons.Rounded.Schedule,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Apple Inset Grouped Section for Activity
                SectionHeader(
                    title = "Recent Transmissions",
                    actionText = "See All",
                    onActionClick = { navController.navigateTab(Screen.Messages.route) },
                )

                Spacer(Modifier.height(10.dp))

                AppleGroupedCard {
                    DemoData.activity.take(4).forEachIndexed { index, msg ->
                        if (index > 0) AppleDivider()
                        ActivityRow(
                            who = msg.senderName,
                            body = msg.body,
                            route = msg.routeHistory,
                            status = msg.status,
                            priority = msg.priority,
                            at = msg.createdAt,
                            onClick = { navController.navigateTab(Screen.Messages.route) },
                        )
                    }
                }
            }
        }

        CmdBar(
            currentRoute = Screen.Dashboard.route,
            onSelect = { navController.navigateTab(it) },
        )
    }
}

@Composable
private fun ActivityRow(
    who: String,
    body: String,
    route: List<String>,
    status: DeliveryStatus,
    priority: Priority,
    at: Long,
    onClick: () -> Unit,
) {
    val pal = Lynq.pal
    val isCritical = priority == Priority.CRITICAL

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        UserAvatar(
            name = who,
            size = 40.dp,
            isCritical = isCritical,
        )

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = who,
                    style = AppleHeadline,
                    color = pal.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = at.asRelative(),
                    style = AppleCaption,
                    color = pal.textMuted,
                )
            }

            Spacer(Modifier.height(2.dp))

            Text(
                text = body,
                style = AppleFootnote,
                color = if (isCritical) pal.emergencyRed else pal.textSecondary,
                maxLines = 1,
            )

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ModernRouteTrace(route = route)
                StatusChip(status = status)
            }
        }
    }
}