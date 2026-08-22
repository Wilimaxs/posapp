package com.project.posapp.feature.cashier.receivable.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.receivable.ReceivableDueStatus
import com.project.posapp.feature.cashier.receivable.ReceivableSort
import com.project.posapp.feature.cashier.receivable.ReceivableUiState
import com.project.posapp.utils.composable.AppFilterButton
import com.project.posapp.utils.composable.AppFilterOption
import com.project.posapp.utils.composable.AppForm

@Composable
fun ReceivableFilterBar(
    state: ReceivableUiState,
    onSearchChange: (String) -> Unit,
    onDueStatusChange: (ReceivableDueStatus?) -> Unit,
    onSortChange: (ReceivableSort) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    val options: List<AppFilterOption<ReceivableDueStatus?>> =
        listOf(
            AppFilterOption<ReceivableDueStatus?>(
                value = null,
                text = "Semua jatuh tempo"
            )
        ) + ReceivableDueStatus.entries.map { status ->
            AppFilterOption(
                value = status,
                text = status.label
            )
        }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .padding(all = Spacing.Standard),
        verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        AppForm(
            value = state.searchQuery,
            onValueChange = onSearchChange,
            placeholder = "Cari nama member atau nomor transaksi",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null
                )
            },
            trailingIcon = if (state.searchQuery.isNotEmpty()) {
                {
                    IconButton(
                        onClick = {
                            onSearchChange("")
                            focusManager.clearFocus()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = "Hapus pencarian"
                        )
                    }
                }
            } else {
                null
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            minHeight = 48.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Tight)
        ) {
            AppFilterButton(
                text = state.dueStatus?.label ?: "Semua jatuh tempo",
                options = options,
                selected = state.dueStatus,
                onSelected = onDueStatusChange,
                modifier = modifier.weight(1f)
            )

            AppFilterButton(
                text = state.sort.label,
                options = ReceivableSort.entries.map { sort ->
                    AppFilterOption(
                        value = sort,
                        text = sort.label
                    )
                },
                selected = state.sort,
                onSelected = onSortChange,
                modifier = modifier.weight(1f)
            )
        }
    }
}