package com.sunless.lynq.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.domain.model.DeliveryStatus
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

@Composable
fun MessagesScreen(navController: NavHostController) {
    var openPeerId by remember { mutableStateOf<String?>(null) }

    androidx.activity.compose.BackHandler(enabled = openPeerId != null) {
        openPeerId = null
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (openPeerId == null) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
            ) {
                ThreadList(onOpen = { openPeerId = it })
            }
        } else {
            OpenThreadView(
                peerId = openPeerId!!,
                onBack = { openPeerId = null },
                modifier = Modifier.weight(1f),
            )
        }

        CmdBar(
            currentRoute = Screen.Messages.route,
            onSelect = { navController.navigateTab(it) },
        )
    }
}

@Composable
private fun ThreadList(onOpen: (String) -> Unit) {
    val pal = Lynq.pal
    TopBar(reachableCount = DemoData.reachableCount, title = "Messages")

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Direct & multi-hop encrypted channels",
            style = AppleSubhead,
            color = pal.textSecondary,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        AppleGroupedCard {
            DemoData.threads.forEachIndexed { index, thread ->
                if (index > 0) AppleDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpen(thread.peer.id) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    UserAvatar(
                        name = thread.peer.name,
                        isRelay = thread.peer.relaying,
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
                                    text = thread.peer.name,
                                    style = AppleHeadline,
                                    fontWeight = FontWeight.SemiBold,
                                    color = pal.textPrimary,
                                )
                                if (thread.unread > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(pal.accentBlue, CircleShape),
                                    )
                                }
                            }

                            Text(
                                text = thread.lastAt.asRelative(),
                                style = AppleCaption,
                                color = pal.textMuted,
                            )
                        }

                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = thread.preview,
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
                            StatusChip(status = thread.status)
                            Text(
                                text = if (thread.peer.hops == 1) "Direct Link" else "${thread.peer.hops} Hops Relay",
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
    peerId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pal = Lynq.pal
    val peer = DemoData.peers.firstOrNull { it.id == peerId } ?: return
    var messageText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        // Apple Style Seamless Conversation Header (No card background or border)
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
                    text = if (peer.hops == 1) "Direct Link · Encrypted" else "${peer.hops} Hops Relay · Encrypted",
                    style = AppleCaption,
                    color = pal.accentBlue,
                )
            }
        }

        AppleDivider()

        // Message Bubbles Container (takes available space, scrolls smoothly)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            data class DemoChat(
                val isMine: Boolean,
                val body: String,
                val time: Long,
                val status: DeliveryStatus,
                val route: List<String>,
            )

            val now = System.currentTimeMillis()
            val chatList = listOf(
                DemoChat(false, "Are you all safe? Cellular and internet are completely down here.", now - 45 * 60_000, DeliveryStatus.DELIVERED, listOf(peer.name, "You")),
                DemoChat(true, "We're safe. We formed a mesh at the east shelter with battery packs.", now - 44 * 60_000, DeliveryStatus.DELIVERED, listOf("You", peer.name)),
                DemoChat(false, "Got it, two more people just connected through Aarav's phone.", now - 8 * 60_000, DeliveryStatus.DELIVERED, listOf(peer.name, "You")),
                DemoChat(true, "Keep Lynq running in background. All packets will forward seamlessly.", now - 6 * 60_000, DeliveryStatus.DELIVERED, listOf("You", "Meera", peer.name)),
            )

            chatList.forEach { chat ->
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

                    // Route breadcrumb
                    Spacer(Modifier.height(2.dp))
                    ModernRouteTrace(route = chat.route)
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
                .padding(horizontal = 16.dp, vertical = 6.dp),
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
                        .clickable { messageText = "" },
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