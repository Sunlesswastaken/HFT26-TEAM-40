package com.sunless.lynq.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.domain.model.DeliveryStatus
import com.sunless.lynq.domain.model.LynqMessage
import com.sunless.lynq.domain.model.Peer
import com.sunless.lynq.ui.components.AppleDivider
import com.sunless.lynq.ui.components.AppleGroupedCard
import com.sunless.lynq.ui.components.CmdBar
import com.sunless.lynq.ui.components.ModernRouteTrace
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
import com.sunless.lynq.util.asClock
import com.sunless.lynq.util.asRelative

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunless.lynq.ui.LynqViewModel

private data class ChatBubble(
    val isMine: Boolean,
    val body: String,
    val time: Long,
    val status: DeliveryStatus,
    val route: List<String>,
)

@Composable
fun MessagesScreen(
    navController: NavHostController,
    viewModel: LynqViewModel = viewModel(),
) {
    val openPeerId by viewModel.openPeerId.collectAsStateWithLifecycle()
    val peers by viewModel.peers.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val reachableCount by viewModel.reachableCount.collectAsStateWithLifecycle()

    val effectivePeers = if (peers.isNotEmpty()) peers else DemoData.peers
    val effectiveReachable = if (peers.isNotEmpty()) reachableCount else DemoData.reachableCount

    androidx.activity.compose.BackHandler(enabled = openPeerId != null) {
        viewModel.openThread(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    ) {
        if (openPeerId == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
            ) {
                ThreadList(
                    reachableCount = effectiveReachable,
                    peers = effectivePeers,
                    messages = messages,
                    onOpen = { viewModel.openThread(it) },
                )
            }
        } else {
            val selectedPeer = effectivePeers.firstOrNull { it.id == openPeerId } ?: effectivePeers.first()
            OpenThreadView(
                peer = selectedPeer,
                messages = messages
                    .filter { it.senderId == selectedPeer.id || it.recipientId == selectedPeer.id }
                    .sortedBy { it.createdAt },
                onSend = { text -> viewModel.sendChatMessage(selectedPeer, text) },
                onBack = { viewModel.openThread(null) },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun ThreadList(
    reachableCount: Int,
    peers: List<Peer>,
    messages: List<LynqMessage>,
    onOpen: (String) -> Unit,
) {
    val pal = Lynq.pal
    TopBar(reachableCount = reachableCount, title = "Messages")

    val sortedPeers = remember(peers, messages) {
        peers.sortedWith(
            compareByDescending<Peer> { peer ->
                messages.filter { it.senderId == peer.id || it.recipientId == peer.id }
                    .maxOfOrNull { it.createdAt } ?: 0L
            }.thenByDescending { it.connected }
            .thenBy { it.name.lowercase() }
            .thenBy { it.id }
        )
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Direct & multi-hop encrypted channels",
            style = AppleSubhead,
            color = pal.textSecondary,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        AppleGroupedCard {
            sortedPeers.forEachIndexed { index, peer ->
                val peerMessages = messages.filter { it.senderId == peer.id || it.recipientId == peer.id }
                val latest = peerMessages.maxByOrNull { it.createdAt }
                val preview = latest?.body ?: "Encrypted channel ready (${if (peer.hops == 1) "direct" else "${peer.hops} hops"})"
                val lastAt = latest?.createdAt ?: peer.lastSeen
                val status = latest?.status ?: DeliveryStatus.DELIVERED

                if (index > 0) AppleDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpen(peer.id) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    UserAvatar(
                        name = peer.name,
                        isRelay = peer.relaying,
                        size = 46.dp,
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                Text(
                                    text = peer.name,
                                    style = AppleHeadline,
                                    fontWeight = FontWeight.SemiBold,
                                    color = pal.textPrimary,
                                )
                            }

                            Text(
                                text = lastAt.asRelative(),
                                style = AppleCaption,
                                color = pal.textMuted,
                            )
                        }

                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = preview,
                            style = AppleSubhead,
                            color = pal.textSecondary,
                            maxLines = 1,
                        )

                        Spacer(Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            StatusChip(status = status)
                            Text(
                                text = if (peer.hops == 1) "Direct Link" else "${peer.hops} Hops Relay",
                                style = AppleCaption,
                                color = pal.textMuted,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OpenThreadView(
    peer: Peer,
    messages: List<LynqMessage>,
    onSend: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pal = Lynq.pal
    val focusManager = LocalFocusManager.current
    var messageText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    LaunchedEffect(messages.size, scrollState.maxValue) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        // Apple Style Seamless Conversation Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(36.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = pal.accentBlue,
                )
            }

            UserAvatar(name = peer.name, size = 36.dp, isRelay = peer.relaying)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = peer.name,
                    style = AppleHeadline,
                    fontWeight = FontWeight.SemiBold,
                    color = pal.textPrimary,
                )
                Text(
                    text = if (peer.hops == 1) "Direct Link · End-to-End Encrypted" else "${peer.hops} Hops Relay · End-to-End Encrypted",
                    style = AppleCaption,
                    color = pal.accentBlue,
                )
            }
        }

        AppleDivider()

        // Message Bubbles Container
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    focusManager.clearFocus()
                }
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val now = System.currentTimeMillis()
            val bubbleList = if (messages.isNotEmpty()) {
                messages.sortedBy { it.createdAt }.map { msg ->
                    ChatBubble(
                        isMine = msg.senderName == "You" || msg.senderId != peer.id,
                        body = msg.body,
                        time = msg.createdAt,
                        status = msg.status,
                        route = if (msg.routeHistory.isNotEmpty()) msg.routeHistory else listOf("You", peer.name),
                    )
                }
            } else {
                listOf(
                    ChatBubble(false, "Are you all safe? Cellular and internet are completely down here.", now - 45 * 60_000, DeliveryStatus.DELIVERED, listOf(peer.name, "You")),
                    ChatBubble(true, "We're safe. We formed a mesh at the east shelter with battery packs.", now - 44 * 60_000, DeliveryStatus.DELIVERED, listOf("You", peer.name)),
                    ChatBubble(false, "Got it, two more people just connected through Aarav's phone.", now - 8 * 60_000, DeliveryStatus.DELIVERED, listOf(peer.name, "You")),
                    ChatBubble(true, "Keep Lynq running in background. All packets will forward seamlessly.", now - 6 * 60_000, DeliveryStatus.DELIVERED, listOf("You", "Meera", peer.name)),
                )
            }

            bubbleList.forEach { chat ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (chat.isMine) Alignment.End else Alignment.Start,
                ) {
                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (chat.isMine) 18.dp else 4.dp,
                            bottomEnd = if (chat.isMine) 4.dp else 18.dp,
                        ),
                        color = if (chat.isMine) pal.accentBlue else pal.surfaceElevated,
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .widthIn(max = 280.dp),
                        ) {
                            Text(
                                text = chat.body,
                                style = AppleBody,
                                color = Color.White,
                            )
                            Spacer(Modifier.height(4.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = chat.time.asClock(),
                                    style = AppleCaption,
                                    color = if (chat.isMine) Color.White.copy(alpha = 0.75f) else pal.textMuted,
                                )
                                if (chat.isMine) {
                                    val (tickIcon, tickColor) = when (chat.status) {
                                        DeliveryStatus.DELIVERED, DeliveryStatus.RELAYED ->
                                            Icons.Rounded.DoneAll to Color.White.copy(alpha = 0.95f)
                                        DeliveryStatus.SENT ->
                                            Icons.Rounded.Check to Color.White.copy(alpha = 0.75f)
                                        DeliveryStatus.PENDING ->
                                            Icons.Rounded.Schedule to Color.White.copy(alpha = 0.6f)
                                        DeliveryStatus.FAILED, DeliveryStatus.EXPIRED ->
                                            Icons.Rounded.ErrorOutline to pal.emergencyRed
                                    }
                                    Icon(
                                        imageVector = tickIcon,
                                        contentDescription = chat.status.name,
                                        tint = tickColor,
                                        modifier = Modifier.size(14.dp),
                                    )
                                }
                            }
                        }
                    }

                    // Route breadcrumb (only show for incoming messages from others)
                    if (!chat.isMine) {
                        Spacer(Modifier.height(2.dp))
                        ModernRouteTrace(route = chat.route)
                    }
                }
            }
        }

        // Apple Style Message Input Capsule - Docked at the bottom
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = pal.surface,
            border = BorderStroke(0.5.dp, pal.border),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 12.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    textStyle = AppleBody.copy(color = pal.textPrimary),
                    cursorBrush = SolidColor(pal.accentBlue),
                    decorationBox = { innerTextField ->
                        if (messageText.isEmpty()) {
                            Text(
                                text = "Encrypted iMessage / Mesh…",
                                style = AppleBody,
                                color = pal.textMuted,
                            )
                        }
                        innerTextField()
                    },
                )

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(pal.accentBlue)
                        .clickable {
                            if (messageText.isNotBlank()) {
                                onSend(messageText.trim())
                                messageText = ""
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}