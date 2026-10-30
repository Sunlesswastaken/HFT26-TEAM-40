package com.sunless.lynq.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.domain.model.Peer
import com.sunless.lynq.ui.components.AppShell
import com.sunless.lynq.ui.components.Hairline
import com.sunless.lynq.ui.components.LynqCard
import com.sunless.lynq.ui.components.PeerAvatar
import com.sunless.lynq.ui.components.RouteTrace
import com.sunless.lynq.ui.components.SectionHeader
import com.sunless.lynq.ui.components.StatusPill
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.ui.theme.MonoStyle
import com.sunless.lynq.util.asRelative
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Mesh Explorer — the "wow" screen. Full topology with the live route of the
 * most recent critical message highlighted, plus relay activity and devices.
 */
@Composable
fun MeshExplorerScreen(navController: NavHostController) {
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
                    text = "Mesh Explorer",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 22.dp),
                )
                Text(
                    text = "Live topology from this device's view.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Lynq.palette.muted,
                    modifier = Modifier.padding(top = 6.dp),
                )

                TopologyCard(Modifier.padding(top = 24.dp))
                RelayActivity(Modifier.padding(top = 32.dp))
                DeviceList(Modifier.padding(top = 32.dp))
            }
        }

        AppShell.BottomBar(
            currentRoute = Screen.MeshExplorer.route,
            onSelect = { navController.navigate(it) },
        )
    }
}

/**
 * Radial topology with a highlighted multi-hop route. Route chords connect
 * consecutive ring neighbours so they never cross the centre node.
 */
@Composable
private fun TopologyCard(modifier: Modifier = Modifier) {
    val palette = Lynq.palette
    val scheme = MaterialTheme.colorScheme
    val peers = DemoData.peers
    // Live route of the latest SOS: You -> Aarav -> Kabir -> Priya.
    val routeNames = listOf("You", "Aarav", "Kabir", "Priya")

    LynqCard(modifier = modifier, contentPadding = 0.dp) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
        ) {
            val centerX = maxWidth / 2
            val centerY = maxHeight / 2
            val ringRadius = minOf(maxWidth, maxHeight) * 0.36f
            val count = peers.size.coerceAtLeast(1)

            val spots = peers.mapIndexed { index, _ ->
                // Ring order matches the route: index 0 top, going clockwise.
                val name = peers[index].name
                val angle = (-90f + index * (360f / count)) * (PI.toFloat() / 180f)
                Triple(
                    name,
                    centerX + ringRadius * cos(angle),
                    centerY + ringRadius * sin(angle),
                )
            }

            Canvas(Modifier.matchParentSize()) {
                val center = Offset(centerX.toPx(), centerY.toPx())
                drawCircle(
                    color = palette.hairline,
                    radius = ringRadius.toPx(),
                    center = center,
                    style = Stroke(width = 1.dp.toPx()),
                )
                spots.forEach { (_, x, y) ->
                    drawLine(
                        color = palette.hairline,
                        start = center,
                        end = Offset(x.toPx(), y.toPx()),
                        strokeWidth = 1.dp.toPx(),
                    )
                }
                // Highlighted route: You (centre) -> peer 0 -> peer 1 -> peer 2
                // (consecutive ring neighbours; chords stay off-centre).
                var prev = center
                spots.take(3).forEach { (_, x, y) ->
                    drawLine(
                        color = palette.sos,
                        start = prev,
                        end = Offset(x.toPx(), y.toPx()),
                        strokeWidth = 2.dp.toPx(),
                    )
                    prev = Offset(x.toPx(), y.toPx())
                }
            }

            // This device.
            Column(
                modifier = Modifier
                    .offset(x = centerX - 34.dp, y = centerY - 26.dp)
                    .width(68.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = scheme.onSurface,
                    contentColor = scheme.surface,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("YOU", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            spots.forEachIndexed { index, (name, x, y) ->
                val peer = peers[index]
                Column(
                    modifier = Modifier
                        .offset(x = x - 34.dp, y = y - 22.dp)
                        .width(68.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    PeerAvatar(
                        name = name,
                        size = 44.dp,
                        highlighted = peer.relaying,
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = peer.name,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "${peer.hops} hop" + if (peer.hops == 1) "" else "s",
                        style = MonoStyle,
                        color = palette.muted,
                    )
                }
            }
        }
        Hairline()
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "CRITICAL",
                    style = MaterialTheme.typography.labelSmall,
                    color = palette.sos,
                )
                RouteTrace(route = routeNames, modifier = Modifier.weight(1f))
                StatusPill(status = com.sunless.lynq.domain.model.DeliveryStatus.DELIVERED)
            }
            Text(
                text = "You → Aarav → Kabir → Priya · Aarav and Meera are carrying traffic",
                style = MaterialTheme.typography.bodySmall,
                color = palette.muted,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun RelayActivity(modifier: Modifier = Modifier) {
    Column(modifier) {
        SectionHeader(title = "Relay activity")
        LynqCard(modifier = Modifier.padding(top = 12.dp), contentPadding = 0.dp) {
            DemoData.activity.forEachIndexed { index, message ->
                if (index > 0) Hairline()
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        val label = when (message.priority) {
                            com.sunless.lynq.domain.model.Priority.CRITICAL -> "SOS"
                            com.sunless.lynq.domain.model.Priority.HIGH -> "High"
                            com.sunless.lynq.domain.model.Priority.NORMAL -> "Message"
                        }
                        Text("$label · ${message.body}", style = MaterialTheme.typography.bodyMedium)
                        RouteTrace(
                            route = message.routeHistory,
                            modifier = Modifier.padding(top = 3.dp),
                        )
                    }
                    StatusPill(status = message.status)
                }
            }
        }
    }
}

@Composable
private fun DeviceList(modifier: Modifier = Modifier) {
    Column(modifier) {
        SectionHeader(title = "Devices")
        LynqCard(modifier = Modifier.padding(top = 12.dp), contentPadding = 0.dp) {
            DemoData.peers.forEachIndexed { index, peer ->
                if (index > 0) Hairline()
                DeviceRow(peer)
            }
        }
    }
}

@Composable
private fun DeviceRow(peer: Peer) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PeerAvatar(name = peer.name, highlighted = peer.relaying)
        Column(modifier = Modifier.weight(1f)) {
            Text(peer.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${peer.id.take(8)} · ${peer.hops} ${if (peer.hops == 1) "hop" else "hops"} · seen ${peer.lastSeen.asRelative()}",
                style = MonoStyle,
                color = Lynq.palette.muted,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
        Text(
            text = if (peer.relaying) "RELAYING" else "LINKED",
            style = MaterialTheme.typography.labelSmall,
            color = if (peer.relaying) Lynq.palette.signal else Lynq.palette.muted,
        )
    }
}