package com.sunless.lynq.ui.screens

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.ui.components.AsciiMesh
import com.sunless.lynq.ui.components.BracketPanel
import com.sunless.lynq.ui.components.CmdBar
import com.sunless.lynq.ui.components.NodeAvatar
import com.sunless.lynq.ui.components.Readout
import com.sunless.lynq.ui.components.RouteTrace
import com.sunless.lynq.ui.components.SecHead
import com.sunless.lynq.ui.components.SosLever
import com.sunless.lynq.ui.components.StatusTag
import com.sunless.lynq.ui.components.SysBar
import com.sunless.lynq.ui.components.Tag
import com.sunless.lynq.ui.components.TagTone
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.ui.theme.TerminalStyle
import com.sunless.lynq.util.asRelative

/** Boot-line wrapper: [18:41:02] text + optional blinking cursor. */
@Composable
private fun BootLine(text: String, cursor: Boolean = false) {
    val pal = Lynq.pal
    val transition = rememberInfiniteTransition(label = "cursor")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1100)),
        label = "blink",
    )
    Row {
        Text(text = text, style = TerminalStyle, color = pal.phFaint)
        if (cursor) {
            Text(
                text = "_",
                style = TerminalStyle,
                color = pal.ph.copy(alpha = alpha),
            )
        }
    }
}

@Composable
fun DashboardScreen(navController: NavHostController) {
    val pal = Lynq.pal
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
        ) {
            SysBar(reachableCount = DemoData.reachableCount)

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // boot log — the terminal is alive
                Column(
                    modifier = Modifier.padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    BootLine("[18:41:02] mesh online · 4 nodes reachable")
                    BootLine("[18:41:02] relay duty: AARAV, MEERA")
                    BootLine("[18:41:07] queue: 3 msgs held for route", cursor = true)
                }

                Text(
                    text = "Network\nDashboard",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 22.dp),
                )

                // readouts
                Row(
                    modifier = Modifier.padding(top = 26.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Readout("${DemoData.reachableCount}", "Reachable", Modifier.weight(1f))
                    Readout("${DemoData.relayCount}", "Relaying", Modifier.weight(1f))
                    Readout(
                        "${DemoData.pendingMessages.size}", "Queued",
                        Modifier.weight(1f), tint = pal.warn,
                    )
                }

                // topology
                Spacer(Modifier.height(26.dp))
                SecHead("Topology", trailing = {
                    Text("full >", style = TerminalStyle, color = pal.phDim)
                })
                BracketPanel(modifier = Modifier.padding(top = 10.dp)) {
                    AsciiMesh(
                        peers = DemoData.peers,
                        liveRoute = listOf("You", "Aarav", "Kabir", "Priya"),
                    )
                    Text(
                        text = "2 links direct · 2 via relay",
                        style = MaterialTheme.typography.bodySmall,
                        color = pal.phFaint,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                }

                // activity
                Spacer(Modifier.height(26.dp))
                SecHead("Activity", trailing = {
                    Text("all msgs >", style = TerminalStyle, color = pal.phDim)
                })
                androidx.compose.foundation.layout.Box {
                    Column {
                        DemoData.activity.forEachIndexed { i, msg ->
                            if (i > 0) {
                                Spacer(Modifier.height(8.dp))
                            }
                            ActivityRow(msg.senderName, msg.body, msg.routeHistory, msg.status, msg.priority, msg.createdAt)
                        }
                    }
                }

                // quick SOS
                SosLever(
                    onClick = { navController.navigate(Screen.Emergency.route) },
                    modifier = Modifier.padding(top = 26.dp),
                )
            }
        }

        CmdBar(
            currentRoute = Screen.Dashboard.route,
            onSelect = { navController.navigate(it) },
        )
    }
}

@Composable
private fun ActivityRow(
    who: String,
    body: String,
    route: List<String>,
    status: com.sunless.lynq.domain.model.DeliveryStatus,
    priority: com.sunless.lynq.domain.model.Priority,
    at: Long,
) {
    val pal = Lynq.pal
    androidx.compose.material3.Surface(
        shape = MaterialTheme.shapes.small,
        color = pal.panel,
        border = androidx.compose.foundation.BorderStroke(1.dp, pal.line),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NodeAvatar(name = who, crit = priority == com.sunless.lynq.domain.model.Priority.CRITICAL)
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(who.uppercase(), style = MaterialTheme.typography.titleMedium)
                    if (priority != com.sunless.lynq.domain.model.Priority.NORMAL) {
                        Tag(
                            text = if (priority == com.sunless.lynq.domain.model.Priority.CRITICAL) "SOS" else "HIGH",
                            tone = if (priority == com.sunless.lynq.domain.model.Priority.CRITICAL) TagTone.CRIT else TagTone.WARN,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = at.asRelative(),
                        style = MaterialTheme.typography.labelMedium,
                        color = pal.phFaint,
                    )
                }
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = pal.phDim,
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    RouteTrace(route = route)
                    StatusTag(status = status)
                }
            }
        }
    }
}