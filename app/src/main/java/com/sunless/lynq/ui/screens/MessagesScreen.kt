package com.sunless.lynq.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.ui.components.AppShell
import com.sunless.lynq.ui.components.Hairline
import com.sunless.lynq.ui.components.LynqCard
import com.sunless.lynq.ui.components.PeerAvatar
import com.sunless.lynq.ui.components.PriorityMark
import com.sunless.lynq.ui.components.StatusPill
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.util.asClock
import com.sunless.lynq.util.asRelative
import com.sunless.lynq.util.shortId

/**
 * Messages — thread list first; an open thread shows the route each message
 * took through the mesh (the feature ordinary chat apps don't have).
 */
@Composable
fun MessagesScreen(navController: NavHostController) {
    var openThreadId by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
        ) {
            when (val id = openThreadId) {
                null -> ThreadList(
                    onOpen = { openThreadId = it },
                )
                else -> OpenThread(
                    threadId = id,
                    onBack = { openThreadId = null },
                )
            }
        }

        AppShell.BottomBar(
            currentRoute = Screen.Messages.route,
            onSelect = { navController.navigate(it) },
        )
    }
}

@Composable
private fun ThreadList(onOpen: (String) -> Unit) {
    AppShell.TopBar(reachableCount = DemoData.reachableCount)

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "Messages",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 22.dp),
        )

        LynqCard(
            modifier = Modifier.padding(top = 32.dp),
            contentPadding = 0.dp,
        ) {
            DemoData.threads.forEachIndexed { index, thread ->
                if (index > 0) Hairline()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    PeerAvatar(name = thread.peer.name)
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(thread.peer.name, style = MaterialTheme.typography.titleMedium)
                            PriorityMark(
                                priority = thread.priority,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                            Text(
                                text = thread.lastAt.asRelative(),
                                style = MaterialTheme.typography.bodySmall,
                                color = Lynq.palette.muted,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End,
                            )
                        }
                        Text(
                            text = thread.preview,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Lynq.palette.muted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp),
                        )
                        Row(
                            modifier = Modifier.padding(top = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            StatusPill(status = thread.status)
                            if (thread.unread > 0) UnreadBadge(thread.unread)
                        }
                    }
                }
            }
        }

        Text(
            text = "Messages travel device to device. Delivery shows the route each message took through the mesh.",
            style = MaterialTheme.typography.bodySmall,
            color = Lynq.palette.muted,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun UnreadBadge(count: Int) {
    androidx.compose.material3.Surface(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.onSurface,
        contentColor = MaterialTheme.colorScheme.surface,
    ) {
        Text(
            text = "$count",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
        )
    }
}

@Composable
fun OpenThread(threadId: String, onBack: () -> Unit) {
    // Header: back, peer, id/hops, delivery state.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "←",
            style = MaterialTheme.typography.titleLarge,
            color = Lynq.palette.muted,
            modifier = Modifier
                .padding(end = 4.dp)
                .clickable(onClick = onBack),
        )
        val peer = DemoData.peers.firstOrNull { it.id == threadId } ?: DemoData.peers.first()
        PeerAvatar(name = peer.name, size = 34.dp)
        Column(modifier = Modifier.weight(1f)) {
            Text(peer.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${peer.id.shortId()} · ${peer.hops} " + if (peer.hops == 1) "hop" else "hops",
                style = com.sunless.lynq.ui.theme.MonoStyle,
                color = Lynq.palette.muted,
            )
        }
        StatusPill(status = DemoData.threads.firstOrNull()?.status ?: com.sunless.lynq.domain.model.DeliveryStatus.DELIVERED)
    }
    Hairline()

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        ThreadBubbles(threadId)
    }
}

@Composable
private fun ThreadBubbles(threadId: String) {
    // Demo conversation matching the mockup until the engine wires real history.
    data class DemoMsg(
        val mine: Boolean,
        val body: String,
        val at: Long,
        val status: com.sunless.lynq.domain.model.DeliveryStatus,
        val route: List<String>,
    )
    val now = System.currentTimeMillis()
    val msgs = remember(threadId) {
        listOf(
            DemoMsg(false, "Are you all safe? Nobody has signal here.", now - 47 * 60_000, com.sunless.lynq.domain.model.DeliveryStatus.DELIVERED, listOf("Aarav", "You")),
            DemoMsg(true, "We're fine. Staying near the east exit.", now - 46 * 60_000, com.sunless.lynq.domain.model.DeliveryStatus.DELIVERED, listOf("You", "Aarav")),
            DemoMsg(false, "Reached the shelter, two more with me.", now - 9 * 60_000, com.sunless.lynq.domain.model.DeliveryStatus.DELIVERED, listOf("Aarav", "You")),
            DemoMsg(true, "Good. Keep the app open, we'll relay you if the network thins out.", now - 7 * 60_000, com.sunless.lynq.domain.model.DeliveryStatus.RELAYED, listOf("You", "Meera", "Aarav")),
        )
    }
    msgs.forEach { msg ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            horizontalAlignment = if (msg.mine) Alignment.End else Alignment.Start,
        ) {
            androidx.compose.material3.Surface(
                shape = MaterialTheme.shapes.medium,
                color = if (msg.mine) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
                contentColor = if (msg.mine) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                border = if (msg.mine) null else androidx.compose.foundation.BorderStroke(1.dp, Lynq.palette.hairline),
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    Text(msg.body, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = msg.at.asClock() +
                            if (msg.mine) " · ${msg.status.label}" else "",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (msg.mine) {
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.72f)
                        } else {
                            Lynq.palette.muted
                        },
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
            Text(
                text = msg.route.joinToString(" → "),
                style = com.sunless.lynq.ui.theme.MonoStyle,
                color = if (msg.mine) Lynq.palette.signal else Lynq.palette.muted,
                modifier = Modifier.padding(top = 5.dp),
            )
        }
    }
}