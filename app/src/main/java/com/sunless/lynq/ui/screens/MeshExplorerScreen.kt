package com.sunless.lynq.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.domain.model.Priority
import com.sunless.lynq.ui.components.AsciiMesh
import com.sunless.lynq.ui.components.BracketPanel
import com.sunless.lynq.ui.components.CmdBar
import com.sunless.lynq.ui.components.NodeAvatar
import com.sunless.lynq.ui.components.RouteTrace
import com.sunless.lynq.ui.components.SecHead
import com.sunless.lynq.ui.components.StatusTag
import com.sunless.lynq.ui.components.SysBar
import com.sunless.lynq.ui.components.Tag
import com.sunless.lynq.ui.components.TagTone
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.ui.theme.TerminalStyle
import com.sunless.lynq.util.asRelative

@Composable
fun MeshExplorerScreen(navController: NavHostController) {
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
                Text(
                    text = "Mesh Explorer",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 18.dp),
                )
                Text(
                    text = "topology from this device's view",
                    style = MaterialTheme.typography.bodySmall,
                    color = pal.phFaint,
                    modifier = Modifier.padding(top = 6.dp),
                )

                val liveRoute = listOf("You", "Aarav", "Kabir", "Priya")

                // topology
                Spacer(Modifier.height(24.dp))
                SecHead("Topology", trailing = { Tag("SOS ROUTE LIVE", tone = TagTone.CRIT) })
                BracketPanel(modifier = Modifier.padding(top = 10.dp)) {
                    AsciiMesh(peers = DemoData.peers, liveRoute = liveRoute)
                    Text(
                        text = "== live SOS · -- relay link",
                        style = TerminalStyle,
                        color = pal.phFaint,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        RouteTrace(route = liveRoute)
                        StatusTag(status = com.sunless.lynq.domain.model.DeliveryStatus.DELIVERED)
                    }
                }

                // relay activity
                Spacer(Modifier.height(26.dp))
                SecHead("Relay activity")
                DemoData.activity.forEachIndexed { i, msg ->
                    if (i > 0) Spacer(Modifier.height(8.dp))
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = pal.panel,
                        border = BorderStroke(1.dp, pal.line),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Tag(
                                        text = when (msg.priority) {
                                            Priority.CRITICAL -> "SOS"
                                            Priority.HIGH -> "HIGH"
                                            Priority.NORMAL -> "MSG"
                                        },
                                        tone = when (msg.priority) {
                                            Priority.CRITICAL -> TagTone.CRIT
                                            Priority.HIGH -> TagTone.WARN
                                            Priority.NORMAL -> TagTone.NEUTRAL
                                        },
                                    )
                                    Text(
                                        text = msg.body,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = pal.phDim,
                                    )
                                }
                                RouteTrace(route = msg.routeHistory, modifier = Modifier.padding(top = 3.dp))
                            }
                            StatusTag(status = msg.status)
                        }
                    }
                }

                // devices
                Spacer(Modifier.height(26.dp))
                SecHead("Devices", trailing = {
                    Text("${DemoData.peers.size} linked", style = TerminalStyle, color = pal.phFaint)
                })
                DemoData.peers.forEachIndexed { i, peer ->
                    if (i > 0) Spacer(Modifier.height(8.dp))
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = pal.panel,
                        border = BorderStroke(1.dp, pal.line),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            NodeAvatar(name = peer.name, relay = peer.relaying)
                            Column(Modifier.weight(1f)) {
                                Text(peer.name.uppercase(), style = MaterialTheme.typography.titleMedium)
                                Text(
                                    text = "${peer.id.take(8)} · ${peer.hops} ${if (peer.hops == 1) "hop" else "hops"} · seen ${peer.lastSeen.asRelative()}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = pal.phFaint,
                                    modifier = Modifier.padding(top = 2.dp),
                                )
                            }
                            Tag(
                                text = if (peer.relaying) "RELAY" else "LINK",
                                tone = if (peer.relaying) TagTone.OK else TagTone.NEUTRAL,
                            )
                        }
                    }
                }
            }
        }

        CmdBar(
            currentRoute = Screen.MeshExplorer.route,
            onSelect = { navController.navigate(it) },
        )
    }
}