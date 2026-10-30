package com.sunless.lynq.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.ui.components.BracketPanel
import com.sunless.lynq.ui.components.CmdBar
import com.sunless.lynq.ui.components.HopMeter
import com.sunless.lynq.ui.components.HopStep
import com.sunless.lynq.ui.components.NodeAvatar
import com.sunless.lynq.ui.components.Panel
import com.sunless.lynq.ui.components.RouteTrace
import com.sunless.lynq.ui.components.SecHead
import com.sunless.lynq.ui.components.SosLever
import com.sunless.lynq.ui.components.StatusTag
import com.sunless.lynq.ui.components.SysBar
import com.sunless.lynq.ui.components.Tag
import com.sunless.lynq.ui.components.TagTone
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.navigation.navigateTab
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.util.asClock
import com.sunless.lynq.util.asRelative

private val CATEGORIES = listOf("Medical", "Fire", "Rescue", "Other")

@Composable
fun EmergencyScreen(navController: NavHostController) {
    val pal = Lynq.pal
    var category by rememberSaveable { mutableStateOf(CATEGORIES.first()) }
    var note by rememberSaveable { mutableStateOf("") }
    var shareLocation by rememberSaveable { mutableStateOf(true) }
    var sending by rememberSaveable { mutableStateOf(false) }

    // System back gesture returns from live tracking to the compose view.
    androidx.activity.compose.BackHandler(enabled = sending) {
        sending = false
    }

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
                    text = "Emergency\nCentre",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 18.dp),
                )

                if (!sending) {
                    // category
                    Spacer(Modifier.height(24.dp))
                    SecHead("Category")
                    FlowChips(CATEGORIES, selected = category, onSelect = { category = it })

                    // note
                    Spacer(Modifier.height(24.dp))
                    SecHead("Note", trailing = {
                        Text("optional", style = MaterialTheme.typography.bodySmall, color = pal.phFaint)
                    })
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = pal.bg,
                        border = BorderStroke(1.dp, pal.line),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                    ) {
                        Text(
                            text = if (note.isBlank()) "what's happening_" else note,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (note.isBlank()) pal.phFaint else pal.ph,
                            modifier = Modifier.padding(12.dp),
                        )
                    }

                    // location
                    Spacer(Modifier.height(24.dp))
                    SecHead("Options")
                    Panel(modifier = Modifier.padding(top = 10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Attach location", style = MaterialTheme.typography.bodyLarge, color = pal.ph)
                                Text(
                                    "works without it · 12.9716,77.5946",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = pal.phFaint,
                                )
                            }
                            Toggle(checked = shareLocation, onChange = { shareLocation = it })
                        }
                    }

                    // the lever — never gated
                    SosLever(
                        onClick = { sending = true },
                        modifier = Modifier.padding(top = 24.dp),
                    )
                } else {
                    TrackingCard(category, note)
                }

                // received alerts
                Spacer(Modifier.height(26.dp))
                SecHead("Received")
                DemoData.alerts.forEachIndexed { i, alert ->
                    if (i > 0) Spacer(Modifier.height(8.dp))
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = pal.panel,
                        border = BorderStroke(1.dp, pal.line),
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            NodeAvatar(name = alert.senderName, crit = true)
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = alert.senderName.uppercase(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = pal.sos,
                                    )
                                    Tag("SOS", Modifier.padding(start = 8.dp), TagTone.CRIT)
                                    Spacer(Modifier.weight(1f))
                                    Text(
                                        text = alert.createdAt.asRelative(),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = pal.phFaint,
                                    )
                                }
                                Text(
                                    text = alert.body,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = pal.phDim,
                                )
                                RouteTrace(
                                    route = alert.routeHistory,
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                            }
                        }
                    }
                }
            }
        }

        CmdBar(
            currentRoute = Screen.Emergency.route,
            onSelect = { navController.navigateTab(it) },
        )
    }
}

@Composable
private fun FlowChips(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    val pal = Lynq.pal
    Column(
        modifier = Modifier.padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.chunked(2).forEach { rowOptions ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowOptions.forEach { opt ->
                    val sel = opt == selected
                    Surface(
                        onClick = { onSelect(opt) },
                        shape = MaterialTheme.shapes.small,
                        color = if (sel) pal.ph else androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = if (sel) pal.bg else pal.phDim,
                        border = if (sel) null else BorderStroke(1.dp, pal.line),
                    ) {
                        Text(
                            text = opt.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        )
                    }
                }
            }
        }
    }
}

/** Terminal square toggle. */
@Composable
private fun Toggle(checked: Boolean, onChange: (Boolean) -> Unit) {
    val pal = Lynq.pal
    Surface(
        onClick = { onChange(!checked) },
        shape = MaterialTheme.shapes.small,
        color = if (checked) pal.panelHi else pal.panel,
        border = BorderStroke(1.dp, pal.lineHi),
        modifier = Modifier.size(width = 44.dp, height = 24.dp),
    ) {
        Box(contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart) {
            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .size(16.dp)
                    .background(if (checked) pal.ph else pal.phFaint, MaterialTheme.shapes.small),
            )
        }
    }
}

@Composable
private fun TrackingCard(category: String, note: String) {
    val pal = Lynq.pal
    val hops = listOf("You", "Aarav", "Kabir", "Priya")
    val reached = 3

    Spacer(Modifier.height(24.dp))
    SecHead("SOS ${hops.hashCode().toString(16).take(4)}", trailing = { StatusTag(com.sunless.lynq.domain.model.DeliveryStatus.RELAYED) })
    BracketPanel(
        modifier = Modifier.padding(top = 10.dp),
        border = pal.sos,
    ) {
        Text(
            text = "${category.uppercase()} - ${if (note.isBlank()) "no note" else note}",
            style = MaterialTheme.typography.bodyLarge,
            color = pal.sos,
        )
        Text(
            text = "sent ${System.currentTimeMillis().asClock()} · loc attached · ttl 6",
            style = MaterialTheme.typography.labelMedium,
            color = pal.phFaint,
            modifier = Modifier.padding(top = 4.dp),
        )
        hops.forEachIndexed { i, hop ->
            HopStep(
                name = hop,
                time = if (i < reached) "18:42:0${i + 3}" else null,
                done = i < reached,
            )
        }
        HopMeter(reached = reached, total = 6, modifier = Modifier.padding(top = 10.dp))
    }
}