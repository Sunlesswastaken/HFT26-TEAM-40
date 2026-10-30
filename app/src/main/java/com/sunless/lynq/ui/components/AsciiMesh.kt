package com.sunless.lynq.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sunless.lynq.domain.model.Peer
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.ui.theme.TerminalStyle

/**
 * ASCII mesh topology: YOU at centre, peers around, links drawn with
 * = | -. VT323 makes the character grid read like a CRT screen.
 * A live SOS route can highlight its hops in red.
 */
@Composable
fun AsciiMesh(
    peers: List<Peer>,
    modifier: Modifier = Modifier,
    liveRoute: List<String> = emptyList(),
) {
    val pal = Lynq.pal
    val you = "YOU"
    val top = peers.getOrNull(0)?.name?.uppercase() ?: "—"
    val right = peers.getOrNull(1)?.name?.uppercase() ?: "—"
    val bottom = peers.getOrNull(2)?.name?.uppercase() ?: "—"
    val left = peers.getOrNull(3)?.name?.uppercase() ?: "—"

    val topOnRoute = liveRoute.any { it.equals(top, true) }
    val rightOnRoute = liveRoute.any { it.equals(right, true) }
    val bottomOnRoute = liveRoute.any { it.equals(bottom, true) }

    // color of a node name: red if on the live SOS route
    fun nodeColor(name: String, base: androidx.compose.ui.graphics.Color) =
        if (liveRoute.any { it.equals(name, true) }) pal.sos else base

    @Composable
    fun N(name: String, onRoute: Boolean, relaying: Boolean) {
        Text(
            text = if (onRoute) "[$name]" else name,
            style = TerminalStyle,
            color = when {
                onRoute -> pal.sos
                relaying -> pal.ph
                name == you -> pal.ph
                else -> pal.phDim
            },
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // top node
        if (peers.isNotEmpty()) N(top, topOnRoute, peers[0].relaying)
        // left arm + right arm
        if (peers.size > 1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("|", style = TerminalStyle, color = pal.phFaint)
                Text("|", style = TerminalStyle, color = pal.phFaint)
            }
        }
        // main row: LEFT === CENTRE === RIGHT
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (peers.size > 3) N(left, false, peers[3].relaying) else Spacer(Modifier.height(0.dp))
            Text(
                text = if (liveRoute.any { it.equals(left, true) }) "==" else "--",
                style = TerminalStyle,
                color = pal.phFaint,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
            Text(
                text = "[$you]",
                style = TerminalStyle,
                color = pal.ph,
            )
            Text(
                text = if (rightOnRoute) "==" else "--",
                style = TerminalStyle,
                color = pal.phFaint,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
            if (peers.size > 1) N(right, rightOnRoute, peers[1].relaying) else Spacer(Modifier.height(0.dp))
        }
        // bottom arm
        if (peers.size > 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("|", style = TerminalStyle, color = pal.phFaint)
                Text("|", style = TerminalStyle, color = pal.phFaint)
            }
            N(bottom, bottomOnRoute, peers[2].relaying)
        }
        // hop note
        if (liveRoute.isNotEmpty()) {
            Text(
                text = "live SOS route",
                style = TerminalStyle,
                color = pal.sos,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}