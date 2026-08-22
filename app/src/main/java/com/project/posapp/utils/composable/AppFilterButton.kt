/*
 * Dibuat oleh: Wilimaxs
 * Dibuat pada: 22 Agustus 2026
 * Tujuan:
 * Menampilkan tombol pembuka filter atau dropdown dengan ukuran
 * dan susunan ikon yang konsisten.
 */
package com.project.posapp.utils.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.PosAppTheme
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing

@Composable
fun AppFilterButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(Radius.Default),
        contentPadding = PaddingValues(
            horizontal = Spacing.Standard
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement =
                    Arrangement.spacedBy(Spacing.Tight),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null
                    )
                }

                Text(
                    text = text,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppFilterButtonPreview() {
    PosAppTheme {
        AppFilterButton(
            text = "Semua status",
            onClick = {}
        )
    }
}