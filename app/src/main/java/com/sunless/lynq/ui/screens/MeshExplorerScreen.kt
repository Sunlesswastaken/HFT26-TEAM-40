package com.sunless.lynq.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.NearMe
import androidx.compose.material.icons.rounded.Sensors
import androidx.compose.material.icons.rounded.WifiTethering
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.domain.model.Peer
import com.sunless.lynq.ui.components.AppleDivider
import com.sunless.lynq.ui.components.AppleGroupedCard
import com.sunless.lynq.ui.components.CmdBar
import com.sunless.lynq.ui.components.MeshGraph
import com.sunless.lynq.ui.components.MetricCard
import com.sunless.lynq.ui.components.ModernRouteTrace
import com.sunless.lynq.ui.components.SectionHeader
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
import com.sunless.lynq.ui.theme.TechCodeStyle
import com.sunless.lynq.util.asRelative

@Composable
fun MeshExplorerScreen(navController: NavHostController) {
    val pal = Lynq.pal
    val liveRoute = listOf("You", "Aarav", "Kabir", "Priya")
    var selectedPeerName by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
        ) {
            TopBar(reachableCount = DemoData.reachableCount, title = "Mesh Graph")

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Interactive Obsidian cluster topology · pinch & drag to explore",
                    style = AppleSubhead,
                    color = pal.textSecondary,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                // Protagonist: Expansive Interactive Obsidian Knowledge Graph
                MeshGraph(
                    peers = DemoData.peers,
                    liveRoute = liveRoute,
                    height = 320.dp,
                    interactive = true,
                    onNodeSelected = { selectedPeerName = it },
                )

                Spacer(Modifier.height(16.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    MetricCard(
                        value = "${DemoData.peers.count { it.hops == 1 }}",
                        label = "Direct Links",
                        accentColor = pal.accentBlue,
                        icon = Icons.Rounded.WifiTethering,
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        value = "${DemoData.peers.count { it.hops > 1 }}",
                        label = "Multi-Hop",
                        accentColor = pal.accentGreen,
                        icon = Icons.Rounded.Hub,
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        value = "6",
                        label = "Hop Limit",
                        accentColor = pal.textSecondary,
                        icon = Icons.Rounded.NearMe,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Discovered Nodes
                SectionHeader(
                    title = "Discovered Nodes",
                    actionText = "${DemoData.peers.size} Reachable",
                )
                Spacer(Modifier.height(10.dp))

                AppleGroupedCard {
                    DemoData.peers.forEachIndexed { index, peer ->
                        if (index > 0) AppleDivider()
                        PeerRow(peer = peer)
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Live Relay Packet Log
                SectionHeader(title = "Relay Packet Log")
                Spacer(Modifier.height(10.dp))

                AppleGroupedCard {
                    DemoData.activity.take(3).forEachIndexed { index, msg ->
                        if (index > 0) AppleDivider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = msg.senderName,
                                    style = AppleHeadline,
                                    color = pal.textPrimary,
                                )
                                Spacer(Modifier.height(2.dp))
                                ModernRouteTrace(route = msg.routeHistory)
                            }
                            StatusChip(status = msg.status)
                        }
                    }
                }
            }
        }

        CmdBar(
            currentRoute = Screen.MeshExplorer.route,
            onSelect = { navController.navigateTab(it) },
        )
    }
}

@Composable
private fun PeerRow(peer: Peer) {
    val pal = Lynq.pal
    val isDirect = peer.hops == 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        UserAvatar(
            name = peer.name,
            size = 44.dp,
            isRelay = peer.relaying,
        )

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = peer.name,
                    style = AppleHeadline,
                    fontWeight = FontWeight.SemiBold,
                    color = pal.textPrimary,
                )
                Text(
                    text = if (isDirect) "Direct link" else "${peer.hops} hops",
                    style = AppleCaption,
                    color = if (isDirect) pal.accentBlue else pal.textMuted,
                )
            }

            Spacer(Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${peer.id.take(8)} · ${if (isDirect) "Direct Connection" else "Relayed via Aarav"}",
                    style = AppleFootnote,
                    color = pal.textSecondary,
                )
                Text(
                    text = peer.lastSeen.asRelative(),
                    style = AppleCaption,
                    color = pal.textMuted,
                )
            }
        }
    }
}