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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sunless.lynq.demo.DemoData
import com.sunless.lynq.ui.components.BracketPanel
import com.sunless.lynq.ui.components.CmdBar
import com.sunless.lynq.ui.components.Panel
import com.sunless.lynq.ui.components.SecHead
import com.sunless.lynq.ui.components.SysBar
import com.sunless.lynq.ui.components.Tag
import com.sunless.lynq.ui.components.TagTone
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.Lynq

@Composable
fun SettingsScreen(navController: NavHostController) {
    val pal = Lynq.pal
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
        ) {
            SysBar(reachableCount = DemoData.reachableCount)

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "System",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 18.dp),
                )

                // identity
                Spacer(Modifier.height(24.dp))
                SecHead("Identity", trailing = {
                    Text("node id", style = MaterialTheme.typography.bodySmall, color = pal.phFaint)
                })
                BracketPanel(modifier = Modifier.padding(top = 10.dp)) {
                    Text(
                        text = DemoData.MY_NAME.uppercase(),
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 24.sp,
                    )
                    Text(
                        text = "${DemoData.MY_DEVICE} · key verified",
                        style = MaterialTheme.typography.bodySmall,
                        color = pal.phFaint,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = pal.bg,
                        border = BorderStroke(1.dp, pal.lineHi),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                    ) {
                        Text(
                            text = "9f3c41d8 a7e5 4b02\nc1aa 77e0 d3f9 21b4",
                            style = MaterialTheme.typography.labelMedium,
                            color = pal.ph,
                            modifier = Modifier.padding(10.dp),
                        )
                    }
                }
                Text(
                    text = "Your ID is the fingerprint of this device's key. Peers verify every message you sign with it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = pal.phFaint,
                    modifier = Modifier.padding(top = 8.dp),
                )

                // security
                Spacer(Modifier.height(26.dp))
                SecHead("Security")
                SettingRows(listOf(
                    Triple("E2E encryption", "AES-256-GCM", "ON" to TagTone.OK),
                    Triple("Signed identity", "Ed25519", "ON" to TagTone.OK),
                    Triple("Broadcast room", "readable by design", "OPEN" to TagTone.NEUTRAL),
                ))

                // mesh
                Spacer(Modifier.height(26.dp))
                SecHead("Mesh")
                Panel(modifier = Modifier.padding(top = 10.dp)) {
                    listOf(
                        "Relay for others" to "ALWAYS",
                        "Discovery" to "CONTINUOUS",
                        "Message retention" to "6 HOURS",
                        "Frames relayed today" to "127",
                    ).forEachIndexed { i, (k, v) ->
                        if (i > 0) Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(k, style = MaterialTheme.typography.bodyMedium, color = pal.phDim)
                            Text(v, style = MaterialTheme.typography.labelMedium, color = pal.ph)
                        }
                    }
                }

                // permissions
                Spacer(Modifier.height(26.dp))
                SecHead("Permissions")
                SettingRows(listOf(
                    Triple("Nearby devices", "", "GRANTED" to TagTone.OK),
                    Triple("Location", "SOS only", "NOT SET" to TagTone.WARN),
                    Triple("Notifications", "", "GRANTED" to TagTone.OK),
                ))

                Text(
                    text = "LYNQ relays between nearby devices only. It cannot reach emergency services without a cellular or internet path.",
                    style = MaterialTheme.typography.bodySmall,
                    color = pal.phFaint,
                    modifier = Modifier.padding(top = 26.dp),
                )
            }
        }

        CmdBar(
            currentRoute = Screen.Settings.route,
            onSelect = { navController.navigate(it) },
        )
    }
}

@Composable
private fun SettingRows(rows: List<Triple<String, String, Pair<String, TagTone>>>) {
    val pal = Lynq.pal
    Column(modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { (title, sub, tagSpec) ->
            Surface(
                shape = MaterialTheme.shapes.small,
                color = pal.panel,
                border = BorderStroke(1.dp, pal.line),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.weight(1f)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(title, style = MaterialTheme.typography.bodyMedium, color = pal.ph)
                            if (sub.isNotEmpty()) {
                                Text(sub, style = MaterialTheme.typography.labelMedium, color = pal.phFaint)
                            }
                        }
                    }
                    Tag(text = tagSpec.first, tone = tagSpec.second)
                }
            }
        }
    }
}