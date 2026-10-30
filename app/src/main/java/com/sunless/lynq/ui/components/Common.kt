package com.sunless.lynq.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sunless.lynq.domain.model.DeliveryStatus
import com.sunless.lynq.domain.model.Priority
import com.sunless.lynq.ui.theme.Lynq
import com.sunless.lynq.ui.theme.MonoStyle

/** Uppercase, letter-spaced label used above every section. */
@Composable
fun Eyebrow(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Lynq.palette.muted,
) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = color,
    )
}

@Composable
fun Hairline(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Lynq.palette.hairline),
    )
}

/** Flat card: 1dp hairline border, no shadow. The only container shape in the app. */
@Composable
fun LynqCard(
    modifier: Modifier = Modifier,
    contentPadding: Dp = 20.dp,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = containerColor,
        border = BorderStroke(1.dp, Lynq.palette.hairline),
    ) {
        Column(Modifier.padding(contentPadding), content = content)
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    trailing: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Eyebrow(title)
        trailing?.invoke(this)
    }
}

/** Big number + tiny caption. */
@Composable
fun StatTile(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface,
) {
    Column(modifier) {
        Text(
            text = value,
            style = MaterialTheme.typography.displayMedium,
            color = tint,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Lynq.palette.muted,
        )
    }
}

@Composable
fun StatusPill(
    status: DeliveryStatus,
    modifier: Modifier = Modifier,
    labelOverride: String? = null,
    positive: Boolean? = null,
) {
    val palette = Lynq.palette
    val scheme = MaterialTheme.colorScheme
    val signalStates = listOf(DeliveryStatus.DELIVERED, DeliveryStatus.RELAYED)
    val (fg, bg) = when {
        status == DeliveryStatus.FAILED || status == DeliveryStatus.EXPIRED ->
            palette.sos to palette.sosSoft
        positive == true || (positive == null && status in signalStates) ->
            palette.signal to palette.signal.copy(alpha = 0.12f)
        else -> scheme.onSurfaceVariant to scheme.surfaceVariant
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = bg,
        contentColor = fg,
    ) {
        Text(
            text = (labelOverride ?: status.label).uppercase(),
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

/** Only meaningful for priority above NORMAL, so NORMAL renders nothing. */
@Composable
fun PriorityMark(priority: Priority, modifier: Modifier = Modifier) {
    if (priority == Priority.NORMAL) return
    val palette = Lynq.palette
    Text(
        text = if (priority == Priority.CRITICAL) "CRITICAL" else "HIGH",
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = if (priority == Priority.CRITICAL) palette.sos else palette.warn,
    )
}

/** `Aarav → Meera → You` */
@Composable
fun RouteTrace(
    route: List<String>,
    modifier: Modifier = Modifier,
    color: Color = Lynq.palette.muted,
) {
    Text(
        text = if (route.isEmpty()) "direct" else route.joinToString("  →  "),
        modifier = modifier,
        style = MonoStyle,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun PeerAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    highlighted: Boolean = false,
) {
    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = if (highlighted) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        contentColor = if (highlighted) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        },
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = name.take(1).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

/** Small neutral chip used for categories and meta. */
@Composable
fun MetaChip(
    text: String,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = containerColor,
        contentColor = contentColor,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
