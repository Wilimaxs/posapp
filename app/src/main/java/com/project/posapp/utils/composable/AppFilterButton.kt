/*
 * Dibuat oleh: Wilimaxs
 * Dibuat pada: 22 Agustus 2026
 * Tujuan:
 * Menampilkan tombol filter beserta dropdown pilihan secara terpusat.
 * Mendukung filter sederhana dan konten dropdown khusus seperti
 * pemilihan rentang tanggal.
 */
package com.project.posapp.utils.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.PosAppTheme
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing

data class AppFilterOption<T>(
    val value: T,
    val text: String,
    val enabled: Boolean = true,
    val dismissOnSelect: Boolean = true
)

@Composable
fun <T> AppFilterButton(
    text: String,
    options: List<AppFilterOption<T>>,
    selected: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    dropdownModifier: Modifier = Modifier,
    customDropdownContent: (@Composable ColumnScope.(dismiss: () -> Unit) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(value = false) }

    Box(
        modifier = modifier
    ) {
        OutlinedButton(
            onClick = {
                expanded = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(size = Radius.Default),
            contentPadding = PaddingValues(horizontal = Spacing.Standard)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Tight),
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

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = dropdownModifier
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.text
                        )
                    },
                    enabled = option.enabled,
                    onClick = {
                        onSelected(option.value)

                        if (option.dismissOnSelect) {
                            expanded = false
                        }
                    },
                    trailingIcon = if (selected == option.value) {
                        {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        null
                    }
                )
            }

            customDropdownContent?.let { content ->
                content(this) {
                    expanded = false
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 600
)
@Composable
private fun AppFilterButtonPreview() {
    PosAppTheme {
        AppFilterButton(
            text = "Semua status",
            options = listOf(
                AppFilterOption(
                    value = "all",
                    text = "Semua status"
                ),
                AppFilterOption(
                    value = "paid",
                    text = "Lunas"
                ),
                AppFilterOption(
                    value = "partial",
                    text = "Cicilan"
                )
            ),
            selected = "all",
            onSelected = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}