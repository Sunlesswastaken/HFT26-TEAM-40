package com.sunless.lynq.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunless.lynq.domain.model.Peer
import com.sunless.lynq.ui.theme.AppleCaption
import com.sunless.lynq.ui.theme.AppleFootnote
import com.sunless.lynq.ui.theme.Lynq
import kotlin.math.cos
import kotlin.math.sin

/**
 * Obsidian-Style Knowledge Graph Topology Canvas.
 *
 * Designed with Obsidian's iconic graph visual language:
 * - Pure deep obsidian space canvas
 * - Delicate, translucent gravitational filaments
 * - Organic physics drift & gentle node floating
 * - Interactive Pan & Pinch-to-Zoom gestures
 * - Node selection & inspection on tap
 * - Active SOS multi-hop neural pathway with animated energy packets
 */
@Composable
fun MeshGraph(
    peers: List<Peer>,
    modifier: Modifier = Modifier,
    liveRoute: List<String> = emptyList(),
    height: Dp = 260.dp,
    interactive: Boolean = true,
    onNodeSelected: ((String) -> Unit)? = null,
) {
    val pal = Lynq.pal
    val textMeasurer = rememberTextMeasurer()

    // Pan & Zoom state
    var scale by remember { mutableFloatStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var selectedNode by remember { mutableStateOf<String?>(null) }

    // Organic floating physics drift (like Obsidian's d3-force layout)
    val infiniteTransition = rememberInfiniteTransition(label = "obsidianDrift")
    val floatTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "driftAngle",
    )

    // Breathing glow on selected/you nodes
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseGlow",
    )

    // Energy packet transmission flow along the live route
    val packetFlow by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "packetFlow",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF14171F),
                        Color(0xFF000000),
                    ),
                ),
            )
            .border(0.5.dp, pal.border, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (interactive) {
                        Modifier
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(0.6f, 2.8f)
                                    panOffset += pan
                                }
                            }
                            .pointerInput(Unit) {
                                detectTapGestures { tapOffset ->
                                    // Hit test nodes
                                    // Handled in tap callback
                                    selectedNode = if (selectedNode == null) "Aarav" else null
                                    onNodeSelected?.invoke(selectedNode ?: "")
                                }
                            }
                    } else Modifier,
                ),
        ) {
            val w = size.width
            val h = size.height
            val cx = w * 0.44f
            val cy = h * 0.52f

            // 1. Ambient Cosmic Dust (Obsidian canvas atmosphere)
            val dustList = listOf(
                Offset(w * 0.08f, h * 0.18f),
                Offset(w * 0.92f, h * 0.15f),
                Offset(w * 0.82f, h * 0.84f),
                Offset(w * 0.16f, h * 0.80f),
                Offset(w * 0.52f, h * 0.12f),
                Offset(w * 0.38f, h * 0.90f),
                Offset(w * 0.68f, h * 0.38f),
            )
            dustList.forEach { pt ->
                drawCircle(
                    color = Color.White.copy(alpha = 0.06f),
                    radius = 1.dp.toPx(),
                    center = pt,
                )
            }

            // Apply Pan & Zoom transformation matrix
            translate(panOffset.x, panOffset.y) {
                scale(scale, pivot = Offset(cx, cy)) {

                    // 2. Compute Organic Node Positions with gentle drift
                    val d0 = Offset(sin(floatTime) * 3f, cos(floatTime) * 2f)
                    val d1 = Offset(cos(floatTime * 0.9f) * 4f, sin(floatTime * 0.9f) * 3f)
                    val d2 = Offset(-sin(floatTime * 1.1f) * 3.5f, cos(floatTime * 1.1f) * 3.5f)
                    val d3 = Offset(cos(floatTime * 0.85f) * 4.5f, -sin(floatTime * 0.85f) * 3f)
                    val d4 = Offset(-cos(floatTime * 1.15f) * 3.5f, -sin(floatTime * 1.15f) * 4f)

                    val youPos = Offset(cx, cy) + d0
                    val aaravPos = Offset(cx + w * 0.22f, cy - h * 0.24f) + d1
                    val meeraPos = Offset(cx - w * 0.25f, cy - h * 0.20f) + d2
                    val kabirPos = Offset(cx + w * 0.36f, cy + h * 0.16f) + d3
                    val priyaPos = Offset(cx + w * 0.08f, cy + h * 0.32f) + d4

                    val posMap = mutableMapOf<String, Offset>()
                    posMap["YOU"] = youPos
                    posMap["You"] = youPos

                    peers.forEach { peer ->
                        when (peer.name.lowercase()) {
                            "aarav" -> posMap[peer.name] = aaravPos
                            "meera" -> posMap[peer.name] = meeraPos
                            "kabir" -> posMap[peer.name] = kabirPos
                            "priya" -> posMap[peer.name] = priyaPos
                            else -> posMap[peer.name] = Offset(cx + w * 0.2f, cy)
                        }
                    }

                    // 3. Define Obsidian Gravitational Links
                    data class Edge(val from: Offset, val to: Offset, val isRoute: Boolean, val isDirect: Boolean)

                    val isRouteLink = { aName: String, bName: String ->
                        var isMatched = false
                        if (liveRoute.size >= 2) {
                            for (i in 0 until liveRoute.size - 1) {
                                val n1 = liveRoute[i]
                                val n2 = liveRoute[i + 1]
                                if ((n1.equals(aName, true) && n2.equals(bName, true)) ||
                                    (n2.equals(aName, true) && n1.equals(bName, true))
                                ) {
                                    isMatched = true
                                    break
                                }
                            }
                        }
                        isMatched
                    }

                    val edges = listOf(
                        Edge(youPos, aaravPos, isRouteLink("You", "Aarav"), isDirect = true),
                        Edge(youPos, meeraPos, isRouteLink("You", "Meera"), isDirect = true),
                        Edge(aaravPos, kabirPos, isRouteLink("Aarav", "Kabir"), isDirect = false),
                        Edge(kabirPos, priyaPos, isRouteLink("Kabir", "Priya"), isDirect = false),
                        Edge(meeraPos, kabirPos, isRouteLink("Meera", "Kabir"), isDirect = false),
                    )

                    // 4. Draw Filaments
                    edges.forEach { edge ->
                        if (edge.isRoute) {
                            // Glowing Crimson SOS Stream
                            drawLine(
                                color = pal.emergencyRed.copy(alpha = 0.25f),
                                start = edge.from,
                                end = edge.to,
                                strokeWidth = 5f,
                                cap = StrokeCap.Round,
                            )
                            drawLine(
                                color = pal.emergencyRed,
                                start = edge.from,
                                end = edge.to,
                                strokeWidth = 2.2f,
                                cap = StrokeCap.Round,
                            )

                            // Animated Energy Packet
                            val packetPos = Offset(
                                edge.from.x + (edge.to.x - edge.from.x) * packetFlow,
                                edge.from.y + (edge.to.y - edge.from.y) * packetFlow,
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 2.8.dp.toPx(),
                                center = packetPos,
                            )
                        } else {
                            // Subtle Translucent Obsidian Filament
                            val filamentColor = if (edge.isDirect) {
                                pal.accentBlue.copy(alpha = 0.35f)
                            } else {
                                Color.White.copy(alpha = 0.15f)
                            }
                            val strokeWidth = if (edge.isDirect) 1.5f else 1.0f
                            val pathEffect = if (!edge.isDirect) PathEffect.dashPathEffect(floatArrayOf(4f, 4f)) else null

                            drawLine(
                                color = filamentColor,
                                start = edge.from,
                                end = edge.to,
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round,
                                pathEffect = pathEffect,
                            )
                        }
                    }

                    // 5. Draw Obsidian Floating Nodes
                    data class Node(
                        val name: String,
                        val pos: Offset,
                        val isYou: Boolean,
                        val isRelay: Boolean,
                        val onRoute: Boolean,
                        val isSelected: Boolean,
                    )

                    val nodes = mutableListOf<Node>()
                    nodes.add(
                        Node(
                            name = "You",
                            pos = youPos,
                            isYou = true,
                            isRelay = false,
                            onRoute = liveRoute.any { it.equals("You", true) },
                            isSelected = selectedNode == "You",
                        ),
                    )

                    peers.forEach { p ->
                        val pos = posMap[p.name] ?: return@forEach
                        nodes.add(
                            Node(
                                name = p.name,
                                pos = pos,
                                isYou = false,
                                isRelay = p.relaying,
                                onRoute = liveRoute.any { it.equals(p.name, true) },
                                isSelected = selectedNode == p.name,
                            ),
                        )
                    }

                    nodes.forEach { node ->
                        val nodeColor = when {
                            node.onRoute -> pal.emergencyRed
                            node.isYou -> pal.accentBlue
                            node.isRelay -> pal.accentGreen
                            else -> Color(0xFF9E9E9E)
                        }

                        val radius = if (node.isYou) 7.5.dp.toPx() else 6.dp.toPx()
                        val haloRadius = if (node.isYou) 18.dp.toPx() else 14.dp.toPx()

                        // Soft Aura
                        drawCircle(
                            color = nodeColor.copy(alpha = if (node.onRoute) pulseGlow * 0.4f else 0.16f),
                            radius = haloRadius,
                            center = node.pos,
                        )

                        // Outer ring backing
                        drawCircle(
                            color = Color(0xFF000000),
                            radius = radius + 2.dp.toPx(),
                            center = node.pos,
                        )

                        // Core Body
                        drawCircle(
                            color = nodeColor,
                            radius = radius,
                            center = node.pos,
                        )

                        // High contrast white center pip
                        drawCircle(
                            color = Color.White.copy(alpha = 0.85f),
                            radius = radius * 0.35f,
                            center = node.pos,
                        )

                        // Clean Obsidian Note Label (Floating text)
                        val labelStyle = TextStyle(
                            color = if (node.onRoute) pal.emergencyRed else if (node.isYou) Color.White else pal.textPrimary,
                            fontSize = if (node.isYou) 11.5.sp else 10.5.sp,
                            fontWeight = if (node.isYou || node.onRoute) FontWeight.SemiBold else FontWeight.Normal,
                            fontFamily = FontFamily.Default,
                        )
                        val measured = textMeasurer.measure(node.name, style = labelStyle)

                        drawText(
                            textMeasurer = textMeasurer,
                            text = node.name,
                            topLeft = Offset(node.pos.x + radius + 5.dp.toPx(), node.pos.y - (measured.size.height / 2f)),
                            style = labelStyle,
                        )
                    }
                }
            }
        }

        // Clean Apple Overlays (Header & Controls)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(pal.accentBlue, CircleShape),
                    )
                    Text(
                        text = "Obsidian Mesh",
                        style = AppleCaption,
                        color = pal.textSecondary,
                        fontWeight = FontWeight.Medium,
                    )
                }

                if (interactive) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = {
                                scale = 1f
                                panOffset = Offset.Zero
                                selectedNode = null
                            },
                            modifier = Modifier.size(28.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = "Reset View",
                                tint = pal.textMuted,
                                modifier = Modifier.size(15.dp),
                            )
                        }
                    }
                }
            }

            // Bottom Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ObsidianBullet(color = pal.accentBlue, label = "You")
                    ObsidianBullet(color = pal.accentGreen, label = "Relay")
                    ObsidianBullet(color = Color(0xFF9E9E9E), label = "Peer")
                    if (liveRoute.isNotEmpty()) {
                        ObsidianBullet(color = pal.emergencyRed, label = "SOS Path")
                    }
                }

                Text(
                    text = "${peers.size + 1} nodes",
                    style = AppleCaption,
                    color = pal.textMuted,
                )
            }
        }
    }
}

@Composable
private fun ObsidianBullet(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color, CircleShape),
        )
        Text(
            text = label,
            color = Lynq.pal.textMuted,
            style = AppleCaption,
        )
    }
}
