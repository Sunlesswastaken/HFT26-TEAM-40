package com.sunless.lynq.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.ui.components.AppShell
import com.sunless.lynq.ui.components.Eyebrow
import com.sunless.lynq.ui.components.Hairline
import com.sunless.lynq.ui.components.LynqCard
import com.sunless.lynq.ui.components.MetaChip
import com.sunless.lynq.ui.components.PeerAvatar
import com.sunless.lynq.ui.components.PriorityMark
import com.sunless.lynq.ui.components.RouteTrace
import com.sunless.lynq.ui.components.SectionHeader
import com.sunless.lynq.ui.components.SosButton
import com.sunless.lynq.ui.components.StatusPill
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.util.asClock
import com.sunless.lynq.util.asRelative

private val CATEGORIES = listOf("Medical", "Fire", "Rescue / Trapped", "Other")

/**
 * Emergency Centre — the centrepiece. Nothing here may block an SOS:
 * category defaults to Medical, note and location are optional, and the
 * send button is always enabled.
 */
@Composable
fun EmergencyScreen(navController: NavHostController) {
    var category by rememberSaveable { mutableStateOf(CATEGORIES.first()) }
    var note by rememberSaveable { mutableStateOf("") }
    var shareLocation by rememberSaveable { mutableStateOf(true) }
    // Stubbed flow: pressing send switches to the live-tracking view until the engine lands.
    var sending by rememberSaveable { mutableStateOf(false) }

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
                    text = "Emergency\nCentre",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 22.dp),
                )

                if (!sending) {
                    ComposeSos(
                        category = category,
                        note = note,
                        shareLocation = shareLocation,
                        onCategory = { category = it },
                        onNote = { note = it },
                        onShareLocation = { shareLocation = it },
                        onSend = { sending = true },
                    )
                } else {
                    TrackingCard(categoryLabel = category, note = note)
                }

                ReceivedAlerts(Modifier.padding(top = 32.dp))
            }
        }

        AppShell.BottomBar(
            currentRoute = Screen.Emergency.route,
            onSelect = { navController.navigate(it) },
        )
    }
}

@Composable
private fun ComposeSos(
    category: String,
    note: String,
    shareLocation: Boolean,
    onCategory: (String) -> Unit,
    onNote: (String) -> Unit,
    onShareLocation: (Boolean) -> Unit,
    onSend: () -> Unit,
) {
    Column(modifier = Modifier.padding(top = 32.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = "Category")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CATEGORIES.take(2).forEach { c ->
                CategoryChip(c, selected = c == category, onClick = { onCategory(c) })
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CATEGORIES.drop(2).forEach { c ->
                CategoryChip(c, selected = c == category, onClick = { onCategory(c) })
            }
        }

        OutlinedTextField(
            value = note,
            onValueChange = onNote,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Add a note (optional)") },
            minLines = 1,
            shape = MaterialTheme.shapes.small,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = Lynq.palette.hairline,
            ),
        )
    }

    Column(modifier = Modifier.padding(top = 32.dp)) {
        SectionHeader(title = "Options")
        LocationToggle(shareLocation = shareLocation, onChange = onShareLocation)
    }

    // SOS is never blocked: no validation, no location requirement, no AI.
    SosButton(
        onClick = onSend,
        modifier = Modifier.padding(top = 20.dp),
        label = "Send SOS",
        supporting = "Broadcasts to every device in range",
    )
}

@Composable
private fun CategoryChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
        color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
        border = if (selected) null else BorderStroke(1.dp, Lynq.palette.hairline),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun LocationToggle(shareLocation: Boolean, onChange: (Boolean) -> Unit) {
    LynqCard(modifier = Modifier.padding(top = 12.dp), contentPadding = 0.dp) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Share location", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Adds your coordinates to the alert. Works without it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Lynq.palette.muted,
                )
            }
            SwitchCompat(checked = shareLocation, onCheckedChange = onChange)
        }
    }
}

/** Minimal switch matching the token set (no Material default colours). */
@Composable
private fun SwitchCompat(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        onClick = { onCheckedChange(!checked) },
        modifier = Modifier
            .width(46.dp)
            .height(28.dp),
        shape = CircleShape,
        color = if (checked) scheme.onSurface else scheme.surfaceVariant,
        border = if (checked) null else BorderStroke(1.dp, Lynq.palette.hairline),
    ) {
        Box(contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart) {
            Surface(
                modifier = Modifier
                    .padding(3.dp)
                    .size(20.dp),
                shape = CircleShape,
                color = if (checked) scheme.surface else Lynq.palette.hairline,
            ) {}
        }
    }
}

@Composable
private fun TrackingCard(categoryLabel: String, note: String) {
    var hopReached by remember { mutableStateOf(3) }
    val hops = listOf("You", "Aarav", "Kabir", "Priya")
    val sentAt = remember { System.currentTimeMillis() - 60_000 }

    Column(modifier = Modifier.padding(top = 32.dp)) {
        SectionHeader(title = "Live tracking", trailing = { StatusPill(status = com.sunless.lynq.domain.model.DeliveryStatus.RELAYED) })
        LynqCard(modifier = Modifier.padding(top = 12.dp)) {
            Text(
                text = if (note.isBlank()) categoryLabel else "$categoryLabel — $note",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "SOS · sent ${sentAt.asClock()}",
                style = com.sunless.lynq.ui.theme.MonoStyle,
                color = Lynq.palette.muted,
                modifier = Modifier.padding(top = 6.dp),
            )
            hops.forEachIndexed { i, name ->
                val done = i < hopReached
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Surface(
                        modifier = Modifier.size(26.dp),
                        shape = CircleShape,
                        color = if (done) Lynq.palette.signal else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (done) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (i == 0 && done) "✓" else name.take(1),
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                    Text(name, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = if (done) "18:4${i + 1}" else "—",
                        style = MaterialTheme.typography.bodySmall,
                        color = Lynq.palette.muted,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.End,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReceivedAlerts(modifier: Modifier = Modifier) {
    Column(modifier) {
        SectionHeader(title = "Received alerts")
        LynqCard(modifier = Modifier.padding(top = 12.dp), contentPadding = 0.dp) {
            DemoData.alerts.forEachIndexed { index, alert ->
                if (index > 0) Hairline()
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    PeerAvatar(name = alert.senderName)
                    Column {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(alert.senderName, style = MaterialTheme.typography.titleMedium)
                            PriorityMark(priority = alert.priority)
                        }
                        Text(alert.body, style = MaterialTheme.typography.bodyMedium)
                        Row(
                            modifier = Modifier.padding(top = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RouteTrace(route = alert.routeHistory)
                            StatusPill(status = alert.status)
                            Text(
                                text = alert.createdAt.asRelative(),
                                style = MaterialTheme.typography.bodySmall,
                                color = Lynq.palette.muted,
                            )
                        }
                    }
                }
            }
        }
    }
}