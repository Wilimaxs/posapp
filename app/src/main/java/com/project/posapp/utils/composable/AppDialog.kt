/*
 * Dibuat oleh: Wilimaxs
 * Dibuat pada: 22 Agustus 2026
 * Tujuan:
 * Menjadi container dialog global dengan ukuran, latar belakang,
 * border, dan perilaku dismiss yang konsisten.
 */
package com.project.posapp.utils.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.project.posapp.core.theme.PosAppTheme
import com.project.posapp.core.theme.Radius

@Composable
fun AppDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    widthFraction: Float = 0.5f,
    maxWidth: Dp = 680.dp,
    maxHeight: Dp = 720.dp,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.40f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth(fraction = widthFraction)
                    .widthIn(max = maxWidth)
                    .heightIn(max = maxHeight)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shape = RoundedCornerShape(size = Radius.Medium)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(size = Radius.Medium)
                    )
            ) {
                content()
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1000, heightDp = 600)
@Composable
private fun AppDialogPreview() {
    PosAppTheme {
        AppDialog(onDismiss = {}) {
            AppBadge(text = "Isi dialog")
        }
    }
}