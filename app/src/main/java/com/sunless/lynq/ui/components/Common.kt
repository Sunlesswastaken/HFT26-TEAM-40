package com.sunless.lynq.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.ui.theme.RouteStyle
import com.sunless.lynq.ui.theme.TerminalStyle

// ---------- CRT overlay: scanlines + vignette ----------

/**
 * Fixed overlay drawing CRT scanlines over the whole screen.
 * Cheap: one Canvas, one drawLine per 3px band, no allocation per frame.
 * Pure decoration — no pointer events.
 */
@Composable
fun CtOverlay(modifier: Modifier = Modifier) {
    val band = Color.Black.copy(alpha = 0.13f)
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val h = constraints.maxHeight.toFloat()
        Canvas(Modifier.matchParentSize()) {
            var y = 0f
            while (y < h) {
                drawLine(
                    color = band,
                    start = Offset(0f, y + 2f),
                    end = Offset(size.width, y + 2f),
                    strokeWidth = 1f,
                )
                y += 3f
            }
        }
    }
}

// ---------- panels ----------

/** The only container in the app: flat panel, hairline border, 2dp radius. */
@Composable
fun Panel(
    modifier: Modifier = Modifier,
    lit: Boolean = false,
    border: Color = if (lit) Lynq.pal.lineHi else Lynq.pal.line,
    contentPadding: Dp = 14.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = Lynq.pal.panel,
        border = BorderStroke(1.dp, border),
    ) {
        Column(Modifier.padding(contentPadding), content = content)
    }
}

/** Section header: pixel label, baseline-aligned trailing slot. */
@Composable
fun SecHead(
    title: String,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Lynq.pal.phDim,
        )
        trailing?.invoke()
    }
}

/** Corner-bracket motif: panel + drawn [ ] corners. Signature element. */
@Composable
fun BracketPanel(
    modifier: Modifier = Modifier,
    border: Color = Lynq.pal.line,
    contentPadding: Dp = 14.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val pal = Lynq.pal
    Box(modifier = modifier) {
        Panel(
            modifier = Modifier.fillMaxSize(),
            lit = true,
            border = border,
            contentPadding = contentPadding,
            content = content,
        )
        Canvas(Modifier.matchParentSize()) {
            val c = 10.dp.toPx()
            // top-left corner
            drawLine(pal.ph, Offset(0f, 1f), Offset(c, 1f), 4f)
            drawLine(pal.ph, Offset(1f, 0f), Offset(1f, c), 4f)
            // bottom-right corner
            drawLine(pal.ph, Offset(size.width - c, size.height - 1f), Offset(size.width, size.height - 1f), 4f)
            drawLine(pal.ph, Offset(size.width - 1f, size.height - c), Offset(size.width - 1f, size.height), 4f)
        }
    }
}

// ---------- tags / status chips ----------

enum class TagTone { OK, WARN, CRIT, NEUTRAL }

/** Pixel status chip: DLV / RLY / SOS / RELAY. */
@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    tone: TagTone = TagTone.NEUTRAL,
) {
    val pal = Lynq.pal
    val (border, fg) = when (tone) {
        TagTone.OK -> pal.lineHi to pal.ph
        TagTone.WARN -> pal.warn to pal.warn
        TagTone.CRIT -> pal.sos to pal.sos
        TagTone.NEUTRAL -> pal.line to pal.phDim
    }
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = Color.Transparent,
        border = BorderStroke(1.dp, border),
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = fg,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
        )
    }
}

/** Delivery status mapped to a terminal tag. */
@Composable
fun StatusTag(status: com.sunless.lynq.domain.model.DeliveryStatus) {
    val (label, tone) = when (status) {
        com.sunless.lynq.domain.model.DeliveryStatus.DELIVERED -> "DLV" to TagTone.OK
        com.sunless.lynq.domain.model.DeliveryStatus.RELAYED -> "RLY" to TagTone.NEUTRAL
        com.sunless.lynq.domain.model.DeliveryStatus.SENT -> "SENT" to TagTone.NEUTRAL
        com.sunless.lynq.domain.model.DeliveryStatus.PENDING -> "QUEUE" to TagTone.WARN
        com.sunless.lynq.domain.model.DeliveryStatus.FAILED, com.sunless.lynq.domain.model.DeliveryStatus.EXPIRED ->
            "FAIL" to TagTone.CRIT
    }
    Tag(text = label, tone = tone)
}

// ---------- route trace ----------

/** `YOU > AARAV > KABIR` — pixel, arrows faint. */
@Composable
fun RouteTrace(
    route: List<String>,
    modifier: Modifier = Modifier,
    highlightLast: Boolean = true,
) {
    val pal = Lynq.pal
    if (route.isEmpty()) {
        Text("direct", style = RouteStyle, color = pal.phDim, modifier = modifier)
        return
    }
    Row(modifier = modifier, verticalAlignment = Alignment.Bottom) {
        route.forEachIndexed { i, hop ->
            if (i > 0) {
                Text(
                    text = ">",
                    style = RouteStyle,
                    color = pal.phFaint,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
            val last = i == route.lastIndex
            Text(
                text = hop.uppercase(),
                style = RouteStyle,
                color = if (last && highlightLast) pal.ph else pal.phDim,
            )
        }
    }
}

// ---------- terminal blocks ----------

/** Square avatar: 1px lit border, pixel initial. Not a circle. */
@Composable
fun NodeAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 38.dp,
    relay: Boolean = false,
    crit: Boolean = false,
) {
    val pal = Lynq.pal
    Surface(
        modifier = modifier.size(size),
        shape = MaterialTheme.shapes.small,
        color = if (relay) pal.ph else pal.panelHi,
        contentColor = if (relay) pal.bg else if (crit) pal.sos else pal.ph,
        border = if (relay) null else BorderStroke(
            1.dp,
            if (crit) pal.sos else pal.lineHi,
        ),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = name.take(1).uppercase(),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

/** Big pixel readout number + tiny caption. */
@Composable
fun Readout(
    value: String,
    caption: String,
    modifier: Modifier = Modifier,
    tint: Color = Lynq.pal.ph,
) {
    Column(modifier) {
        Text(
            text = value,
            style = MaterialTheme.typography.displaySmall,
            color = tint,
        )
        Text(
            text = caption.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Lynq.pal.phFaint,
            modifier = Modifier.padding(top = 5.dp),
        )
    }
}

// ---------- sysbar / cmdbar ----------

/** Top bar: LYNQ_ wordmark + mesh LED status. Same on every screen. */
@Composable
fun SysBar(
    reachableCount: Int,
    online: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val pal = Lynq.pal
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "LYNQ_",
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = com.sunless.lynq.ui.theme.PixelFont,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 24.sp,
                letterSpacing = 1.sp,
            ),
            color = pal.ph,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .padding(end = 7.dp)
                    .size(8.dp)
                    .background(if (online) pal.ph else pal.phFaint, MaterialTheme.shapes.extraSmall),
            )
            Text(
                text = if (online) "MESH:${reachableCount}" else "OFFLINE",
                style = TerminalStyle,
                color = pal.phDim,
            )
        }
    }
}

data class CmdItem(
    val label: String,
    val glyph: String,
    val route: String,
    val isSos: Boolean = false,
)