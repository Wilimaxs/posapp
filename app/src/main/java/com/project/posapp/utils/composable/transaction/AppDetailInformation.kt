/*
 * Dibuat oleh: Wilimaxs
 * Dibuat pada: 22 Agustus 2026
 * Tujuan:
 * Menampilkan pasangan label dan nilai pada informasi detail transaksi.
 * Digunakan bersama oleh fitur History dan Piutang.
 */
package com.project.posapp.utils.composable.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.project.posapp.core.theme.Spacing

@Composable
fun AppDetailInformation(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.Micro)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(name = "App Detail Information", showBackground = true)
@Composable
private fun AppDetailInformationPreview() {
    AppDetailInformation(
        label = "Nama Member",
        value = "Budi Santoso",
        modifier = Modifier.padding(all = Spacing.Standard)
    )
}