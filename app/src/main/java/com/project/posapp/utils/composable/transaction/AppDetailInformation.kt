/*
 * Dibuat oleh: Wilimaxs
 * Dibuat pada: 22 Agustus 2026
 * Tujuan:
 * Menampilkan pasangan label dan nilai pada informasi detail transaksi.
 * Mendukung susunan vertikal maupun horizontal sesuai kebutuhan.
 */
package com.project.posapp.utils.composable.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.project.posapp.core.theme.Spacing

enum class AppDetailInformationLayout {
    VERTICAL,
    HORIZONTAL
}

@Composable
fun AppDetailInformation(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    layout: AppDetailInformationLayout = AppDetailInformationLayout.VERTICAL,
    labelColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    labelStyle: TextStyle = MaterialTheme.typography.bodySmall,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(Spacing.Tight),
) {
    val content: @Composable () -> Unit = {
        Text(
            text = label,
            style = labelStyle,
            fontWeight = FontWeight.Bold,
            color = labelColor
        )

        Text(
            text = value,
            style = valueStyle,
            color = valueColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    when (layout) {
        AppDetailInformationLayout.VERTICAL -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(Spacing.Micro)
            ) {
                content()
            }
        }

        AppDetailInformationLayout.HORIZONTAL -> {
            Row(
                modifier = modifier,
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
        }
    }
}

@Preview(name = "Vertical", showBackground = true)
@Composable
private fun AppDetailInformationVerticalPreview() {
    AppDetailInformation(
        label = "Nama Member",
        value = "Budi Santoso",
        modifier = Modifier.padding(Spacing.Standard)
    )
}

@Preview(name = "Horizontal", showBackground = true)
@Composable
private fun AppDetailInformationHorizontalPreview() {
    AppDetailInformation(
        label = "Metode",
        value = "Tunai",
        layout = AppDetailInformationLayout.HORIZONTAL,
        modifier = Modifier.padding(Spacing.Standard)
    )
}