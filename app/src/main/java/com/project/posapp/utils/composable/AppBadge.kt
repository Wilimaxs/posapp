/*
 * Dibuat oleh: Wilimaxs
 * Dibuat pada: 22 Agustus 2026
 * Tujuan:
 * Menampilkan label atau status singkat dengan warna, border,
 * bentuk, dan konten yang dapat disesuaikan.
 */
package com.project.posapp.utils.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing

@Composable
fun AppBadge(
    modifier: Modifier = Modifier,
    text: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(Radius.Small),
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
    content: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .background(
                color = containerColor,
                shape = shape
            )
            .then(
                other = if (borderWidth > 0.dp) {
                    Modifier.border(
                        width = borderWidth,
                        color = borderColor,
                        shape = shape
                    )
                } else {
                    Modifier
                }
            )
            .padding(
                horizontal = Spacing.Tight,
                vertical = Spacing.Micro
            ),
        contentAlignment = Alignment.Center
    ) {
        if (content != null) {
            content()
        } else {
            text?.let {
                Text(
                    text = it,
                    style = textStyle,
                    color = contentColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBadgePreview() {
    AppBadge(
        text = "Aktif",
        modifier = Modifier.padding(all = Spacing.Standard)
    )
}