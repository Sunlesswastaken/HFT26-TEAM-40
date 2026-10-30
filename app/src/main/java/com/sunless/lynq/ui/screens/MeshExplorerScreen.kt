package com.sunless.lynq.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.FormatListBulleted
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.domain.model.Peer
import com.sunless.lynq.ui.components.AppleDivider
import com.sunless.lynq.ui.components.AppleGroupedCard
import com.sunless.lynq.ui.components.CmdBar
import com.sunless.lynq.ui.components.MeshGraph
import com.sunless.lynq.ui.components.ModernRouteTrace
import com.sunless.lynq.ui.components.SectionHeader
import com.sunless.lynq.ui.components.StatusChip
import com.sunless.lynq.ui.components.TopBar
import com.sunless.lynq.ui.components.UserAvatar
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.navigation.navigateTab
import com.sunless.lynq.ui.theme.AppleCaption
import com.sunless.lynq.ui.theme.AppleFootnote
import com.sunless.lynq.ui.theme.AppleHeadline
import com.sunless.lynq.ui.theme.AppleSubhead
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.util.asRelative
import kotlinx.coroutines.launch

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunless.lynq.ui.LynqViewModel

/**
 * Obsidian-Inspired Mesh Explorer Screen.
 *
 * Protagonist: Expansive Obsidian Knowledge Graph Canvas.
 * - Deep dark charcoal atmosphere
 * - Generous negative space without cluttering dashboard cards
 * - Touch-interactive constellation with live harmonic drift
 * - Tap node to inspect relationships and highlight connected sub-graph
 * - Clean Apple bottom drawer for directory and transmission history
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeshExplorerScreen(
    navController: NavHostController,
    viewModel: LynqViewModel = viewModel(),
) {
    val pal = Lynq.pal
    val peers by viewModel.peers.collectAsStateWithLifecycle()
    val reachableCount by viewModel.reachableCount.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()

    val effectivePeers = if (peers.isNotEmpty()) peers else DemoData.peers
    val effectiveReachable = if (peers.isNotEmpty()) reachableCount else DemoData.reachableCount
    val effectiveActivity = if (messages.isNotEmpty()) messages else DemoData.activity

    val liveRoute = listOf("You", "Aarav", "Kabir", "Priya")
    var selectedPeerName by remember { mutableStateOf<String?>(null) }
    var showDirectorySheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(reachableCount = effectiveReachable, title = "Mesh Graph")

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
        ) {
            // Refined Subhead
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Mesh Constellation · Multi-Hop Topology",
                    style = AppleSubhead,
                    color = pal.textSecondary,
                )
            }

            // Protagonist: Expansive Obsidian Knowledge Graph Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                MeshGraph(
                    peers = effectivePeers,
                    liveRoute = liveRoute,
                    interactive = true,
                    selectedNodeName = selectedPeerName,
                    onNodeSelected = { selectedPeerName = it },
                    onOpenMessage = {
                        navController.navigateTab(Screen.Messages.route)
                    },
                )
            }

            Spacer(Modifier.height(10.dp))

            // Refined Minimalist Obsidian Cluster Status Capsule
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xF41C1C1E),
                border = BorderStroke(0.5.dp, Color(0x33FFFFFF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDirectorySheet = true },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF30D158), CircleShape),
                        )
                        Text(
                            text = "${DemoData.peers.size + 1} Discovered Nodes · 2 Relays · Hop Limit 6",
                            style = AppleCaption,
                            fontSize = 11.sp,
                            color = pal.textSecondary,
                            fontWeight = FontWeight.Medium,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.FormatListBulleted,
                            contentDescription = "Directory",
                            tint = pal.accentBlue,
                            modifier = Modifier.size(15.dp),
                        )
                        Text(
                            text = "Nodes",
                            style = AppleCaption,
                            color = pal.accentBlue,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }

        CmdBar(
            currentRoute = Screen.MeshExplorer.route,
            onSelect = { navController.navigateTab(it) },
        )
    }

    // Modal Peer Directory & Transmission History Sheet
    if (showDirectorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showDirectorySheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF1C1C1E),
            contentColor = pal.textPrimary,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .size(width = 36.dp, height = 4.dp)
                        .background(Color(0x44FFFFFF), RoundedCornerShape(2.dp)),
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp)
                    .padding(bottom = 36.dp),
            ) {
                SectionHeader(
                    title = "Discovered Mesh Nodes",
                    actionText = "${effectivePeers.size} Connected",
                )
                Spacer(Modifier.height(10.dp))

                AppleGroupedCard {
                    effectivePeers.forEachIndexed { index, peer ->
                        if (index > 0) AppleDivider()
                        PeerRow(peer = peer, onClick = {
                            selectedPeerName = peer.name
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                showDirectorySheet = false
                            }
                        })
                    }
                }

                Spacer(Modifier.height(24.dp))

                SectionHeader(title = "Recent Transmissions")
                Spacer(Modifier.height(10.dp))

                AppleGroupedCard {
                    effectiveActivity.take(3).forEachIndexed { index, msg ->
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
    }
}

@Composable
private fun PeerRow(peer: Peer, onClick: () -> Unit) {
    val pal = Lynq.pal
    val isDirect = peer.hops == 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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