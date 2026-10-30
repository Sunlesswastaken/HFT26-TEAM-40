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
import com.sunless.lynq.ui.theme.Lynq

/**
 * The one button that must never be missed. Full width, solid SOS colour, large text.
 * Callers must still confirm before sending (see EmergencyScreen).
 */
@Composable
fun SosButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Send SOS",
    supporting: String = "Broadcasts to every device in range",
    enabled: Boolean = true,
) {
    val palette = Lynq.palette
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = palette.sos,
        contentColor = palette.onSos,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(text = label, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = supporting,
                    style = MaterialTheme.typography.bodySmall,
                    color = palette.onSos.copy(alpha = 0.78f),
                )
            }
            Text(
                text = "CRITICAL",
                style = MaterialTheme.typography.labelSmall,
                color = palette.onSos.copy(alpha = 0.78f),
            )
        }
    }
}
