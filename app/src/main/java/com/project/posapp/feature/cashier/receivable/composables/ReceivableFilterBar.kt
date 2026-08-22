package com.project.posapp.feature.cashier.receivable.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Radius.Medium)
            )
            .padding(Spacing.Standard),
        verticalArrangement =
            Arrangement.spacedBy(Spacing.Tight)
    ) {
        AppForm(
            value = state.searchQuery,
            onValueChange = onSearchChange,
            placeholder =
                "Cari nama member atau nomor transaksi",
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
            horizontalArrangement =
                Arrangement.spacedBy(Spacing.Tight)
        ) {
            DueStatusMenu(
                selectedStatus = state.dueStatus,
                onStatusChange = onDueStatusChange,
                modifier = Modifier.weight(1f)
            )

            SortMenu(
                selectedSort = state.sort,
                onSortChange = onSortChange,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DueStatusMenu(
    selectedStatus: ReceivableDueStatus?,
    onStatusChange: (ReceivableDueStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        AppFilterButton(
            text = selectedStatus?.label
                ?: "Semua jatuh tempo",
            onClick = {
                expanded = true
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                text = {
                    Text("Semua jatuh tempo")
                },
                onClick = {
                    onStatusChange(null)
                    expanded = false
                },
                trailingIcon = if (selectedStatus == null) {
                    {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null
                        )
                    }
                } else {
                    null
                }
            )

            ReceivableDueStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = {
                        Text(status.label)
                    },
                    onClick = {
                        onStatusChange(status)
                        expanded = false
                    },
                    trailingIcon =
                        if (selectedStatus == status) {
                            {
                                Icon(
                                    imageVector =
                                        Icons.Outlined.Check,
                                    contentDescription = null
                                )
                            }
                        } else {
                            null
                        }
                )
            }
        }
    }
}

@Composable
private fun SortMenu(
    selectedSort: ReceivableSort,
    onSortChange: (ReceivableSort) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        AppFilterButton(
            text = selectedSort.label,
            onClick = {
                expanded = true
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            ReceivableSort.entries.forEach { sort ->
                DropdownMenuItem(
                    text = {
                        Text(sort.label)
                    },
                    onClick = {
                        onSortChange(sort)
                        expanded = false
                    },
                    trailingIcon =
                        if (selectedSort == sort) {
                            {
                                Icon(
                                    imageVector =
                                        Icons.Outlined.Check,
                                    contentDescription = null
                                )
                            }
                        } else {
                            null
                        }
                )
            }
        }
    }
}