package com.sunless.lynq.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sunless.lynq.domain.model.Peer
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.ui.theme.MonoStyle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private data class NodeSpot(val x: Dp, val y: Dp)

/**
 * Radial topology: this device in the middle, every reachable peer on the ring.
 * Relaying peers are filled so you can read the mesh at a glance on stage.
 */
@Composable
fun MeshGraph(
    peers: List<Peer>,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    val palette = Lynq.palette

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
    ) {
        val centerX = maxWidth / 2
        val centerY = maxHeight / 2
        val ringRadius = minOf(maxWidth, maxHeight) * 0.35f
        val count = peers.size.coerceAtLeast(1)

        val spots = peers.mapIndexed { index, _ ->
            val angle = (-90f + index * (360f / count)) * (PI.toFloat() / 180f)
            NodeSpot(
                x = centerX + ringRadius * cos(angle),
                y = centerY + ringRadius * sin(angle),
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
            spots.forEach { spot ->
                drawLine(
                    color = palette.hairline,
                    start = center,
                    end = Offset(spot.x.toPx(), spot.y.toPx()),
                    strokeWidth = 1.dp.toPx(),
                )
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

        peers.forEachIndexed { index, peer ->
            val spot = spots[index]
            Column(
                modifier = Modifier
                    .offset(x = spot.x - 34.dp, y = spot.y - 22.dp)
                    .width(68.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PeerAvatar(
                    name = peer.name,
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
}
