package com.sunless.lynq.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sunless.lynq.domain.model.DeliveryStatus
import com.sunless.lynq.ui.theme.Lynq

/**
 * The SOS lever. Full-width, solid red, pixel type — the only solid red
 * element in the app. Never disabled, never gated.
 */
@Composable
fun SosLever(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "HOLD SOS",
    supporting: String = "hold 3s · broadcasts to all in range",
) {
    val pal = Lynq.pal
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = pal.sos,
        contentColor = pal.onSos,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = supporting,
                style = MaterialTheme.typography.bodySmall,
                color = pal.onSos.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

/**
 * Hop meter: [####......] — live progress through the TTL budget.
 */
@Composable
fun HopMeter(
    reached: Int,
    total: Int,
    modifier: Modifier = Modifier,
) {
    val pal = Lynq.pal
    val filled = "#".repeat(reached.coerceIn(0, total))
    val empty = ".".repeat((total - reached).coerceIn(0, total))
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "[", style = MaterialTheme.typography.labelLarge, color = pal.phFaint)
        Text(text = filled, style = MaterialTheme.typography.labelLarge, color = pal.ph)
        Text(text = empty, style = MaterialTheme.typography.labelLarge, color = pal.phFaint)
        Text(text = "]", style = MaterialTheme.typography.labelLarge, color = pal.phFaint)
        Text(
            text = " $reached/$total",
            style = MaterialTheme.typography.labelMedium,
            color = pal.phFaint,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

/**
 * A hop step in the live SOS tracker: [mark] WHO ....... time
 */
@Composable
fun HopStep(
    name: String,
    time: String?,
    done: Boolean,
    modifier: Modifier = Modifier,
) {
    val pal = Lynq.pal
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = if (done) pal.ph else pal.panel,
            border = if (done) null else androidx.compose.foundation.BorderStroke(1.dp, pal.lineHi),
            contentColor = if (done) pal.bg else pal.phDim,
        ) {
            Text(
                text = if (done && name.equals("YOU", true)) "v" else name.take(1).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(4.dp),
            )
        }
        Text(
            text = name.uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (done) pal.ph else pal.phDim,
        )
        Text(
            text = time ?: "- -",
            style = MaterialTheme.typography.labelMedium,
            color = pal.phFaint,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

/** Delivery status -> tag, kept for screens that show status inline. */
@Composable
fun DeliveryTag(status: DeliveryStatus) = StatusTag(status = status)