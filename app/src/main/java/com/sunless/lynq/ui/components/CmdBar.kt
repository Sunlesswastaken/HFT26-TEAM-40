package com.sunless.lynq.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sunless.lynq.ui.navigation.Screen
import com.sunless.lynq.ui.theme.Lynq

/**
 * Command bar: the 5-tab bottom nav. ASCII glyph + pixel label.
 * SOS tab always outlined red; active tab inverts (green fill).
 * 48dp minimum touch targets.
 */
@Composable
fun CmdBar(
    currentRoute: String,
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit,
) {
    val pal = Lynq.pal
    val items = listOf(
        CmdItem("DASH", "[*]", Screen.Dashboard.route),
        CmdItem("MSGS", "[M]", Screen.Messages.route),
        CmdItem("SOS", "[!]", Screen.Emergency.route, isSos = true),
        CmdItem("MESH", "[=]", Screen.MeshExplorer.route),
        CmdItem("SYS", "[$]", Screen.Settings.route),
    )
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = pal.panel,
        border = androidx.compose.foundation.BorderStroke(1.dp, pal.line),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                val active = item.route == currentRoute
                val fg = when {
                    item.isSos && active -> pal.onSos
                    item.isSos -> pal.sos
                    active -> pal.bg
                    else -> pal.phFaint
                }
                val bg = when {
                    item.isSos && active -> pal.sos
                    active -> pal.ph
                    else -> androidx.compose.ui.graphics.Color.Transparent
                }
                Surface(
                    onClick = { onSelect(item.route) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.small,
                    color = bg,
                    border = if (item.isSos && !active) {
                        androidx.compose.foundation.BorderStroke(1.dp, pal.sos)
                    } else null,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = item.glyph,
                            style = MaterialTheme.typography.labelLarge,
                            color = fg,
                        )
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = fg,
                        )
                    }
                }
            }
        }
    }
}