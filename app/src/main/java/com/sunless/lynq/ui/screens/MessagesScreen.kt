package com.sunless.lynq.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.domain.model.DeliveryStatus
import com.sunless.lynq.domain.model.Priority
import com.sunless.lynq.ui.components.CmdBar
import com.sunless.lynq.ui.components.NodeAvatar
import com.sunless.lynq.ui.components.RouteTrace
import com.sunless.lynq.ui.components.StatusTag
import com.sunless.lynq.ui.components.SysBar
import com.sunless.lynq.ui.components.Tag
import com.sunless.lynq.ui.components.TagTone
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.util.asClock
import com.sunless.lynq.util.asRelative

@Composable
fun MessagesScreen(navController: NavHostController) {
    var openId by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
        ) {
            if (openId == null) {
                ThreadList { openId = it }
            } else {
                OpenThread(threadPeerId = openId!!, onBack = { openId = null })
            }
        }
        CmdBar(
            currentRoute = Screen.Messages.route,
            onSelect = { navController.navigate(it) },
        )
    }
}

@Composable
private fun ThreadList(onOpen: (String) -> Unit) {
    val pal = Lynq.pal
    SysBar(reachableCount = DemoData.reachableCount)

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Messages",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 18.dp),
        )

        Spacer(Modifier.height(20.dp))
        DemoData.threads.forEachIndexed { i, thread ->
            if (i > 0) Spacer(Modifier.height(8.dp))
            Surface(
                onClick = { onOpen(thread.peer.id) },
                shape = MaterialTheme.shapes.small,
                color = pal.panel,
                border = BorderStroke(1.dp, pal.line),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    NodeAvatar(name = thread.peer.name)
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = thread.peer.name.uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                            )
                            if (thread.unread > 0) {
                                Text(
                                    text = "${thread.unread}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = pal.bg,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .clickable { }
                                        .let { m ->
                                            m
                                        },
                                )
                            }
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = thread.lastAt.asRelative(),
                                style = MaterialTheme.typography.labelMedium,
                                color = pal.phFaint,
                            )
                        }
                        Text(
                            text = thread.preview,
                            style = MaterialTheme.typography.bodyMedium,
                            color = pal.phDim,
                            maxLines = 1,
                        )
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            StatusTag(status = thread.status)
                            Text(
                                text = "${thread.peer.hops} ${if (thread.peer.hops == 1) "hop" else "hops"}",
                                style = MaterialTheme.typography.labelMedium,
                                color = pal.phFaint,
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "Messages hop device to device. Each shows the route it took.",
            style = MaterialTheme.typography.bodySmall,
            color = pal.phFaint,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun OpenThread(threadPeerId: String, onBack: () -> Unit) {
    val pal = Lynq.pal
    val peer = DemoData.peers.firstOrNull { it.id == threadPeerId } ?: return

    // Header
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "<",
            style = MaterialTheme.typography.headlineMedium,
            color = pal.phDim,
            modifier = Modifier
                .clickable(onClick = onBack)
                .padding(horizontal = 6.dp),
        )
        NodeAvatar(name = peer.name, size = 32.dp)
        Column(modifier = Modifier.weight(1f)) {
            Text(peer.name.uppercase(), style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${peer.id.take(8)} · ${peer.hops} ${if (peer.hops == 1) "hop" else "hops"} · link ok",
                style = MaterialTheme.typography.labelMedium,
                color = pal.phFaint,
            )
        }
        StatusTag(status = DeliveryStatus.DELIVERED)
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        data class Msg(
            val mine: Boolean,
            val body: String,
            val at: Long,
            val status: DeliveryStatus,
            val route: List<String>,
        )
        val now = System.currentTimeMillis()
        listOf(
            Msg(false, "Are you all safe? Nobody has signal here.", now - 47 * 60_000, DeliveryStatus.DELIVERED, listOf("Aarav", "You")),
            Msg(true, "We're fine. Staying near the east exit.", now - 46 * 60_000, DeliveryStatus.DELIVERED, listOf("You", "Aarav")),
            Msg(false, "Reached the shelter, two more with me.", now - 9 * 60_000, DeliveryStatus.DELIVERED, listOf("Aarav", "You")),
            Msg(true, "Good. Keep the app open, we'll relay you if the mesh thins out.", now - 7 * 60_000, DeliveryStatus.RELAYED, listOf("You", "Meera", "Aarav")),
        ).forEach { msg ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = if (msg.mine) Alignment.End else Alignment.Start,
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (msg.mine) pal.panelHi else pal.panel,
                    border = BorderStroke(1.dp, if (msg.mine) pal.lineHi else pal.line),
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                        Text(
                            text = msg.body,
                            style = MaterialTheme.typography.bodyLarge,
                            color = pal.ph,
                        )
                        Text(
                            text = msg.at.asClock() + if (msg.mine) " · ${statusWord(msg.status)}" else "",
                            style = MaterialTheme.typography.labelMedium,
                            color = pal.phFaint,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
                Row(modifier = Modifier.padding(top = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                    RouteTrace(route = msg.route)
                }
            }
        }

        // composer
        Surface(
            shape = MaterialTheme.shapes.small,
            color = pal.panel,
            border = BorderStroke(1.dp, pal.line),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "type message_",
                    style = MaterialTheme.typography.bodyMedium,
                    color = pal.phFaint,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "[SEND]",
                    style = MaterialTheme.typography.labelSmall,
                    color = pal.ph,
                )
            }
        }
    }
}

private fun statusWord(s: DeliveryStatus) = when (s) {
    DeliveryStatus.DELIVERED -> "v"
    DeliveryStatus.RELAYED -> "relaying"
    else -> s.label
}