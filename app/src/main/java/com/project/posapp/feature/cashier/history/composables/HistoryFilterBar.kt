package com.project.posapp.feature.cashier.history.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.history.HistoryDateFilter
import com.project.posapp.feature.cashier.history.HistoryPaymentStatus
import com.project.posapp.feature.cashier.history.HistoryUiState
import com.project.posapp.utils.composable.AppFilterButton
import com.project.posapp.utils.composable.AppForm
import com.project.posapp.utils.composable.PrimaryButton
import com.project.posapp.utils.extensions.showDatePicker
import com.project.posapp.utils.extensions.toDisplayDate
import com.project.posapp.utils.extensions.toLocalDateOrNull
import java.time.LocalDate

@Composable
fun HistoryFilterBar(
    state: HistoryUiState,
    onSearchChange: (String) -> Unit,
    onDateFilterChange: (HistoryDateFilter) -> Unit,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onApplyCustomFilter: () -> Unit,
    onPaymentStatusChange: (HistoryPaymentStatus?) -> Unit,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(Radius.Medium)
            )
            .padding(Spacing.Standard),
        verticalArrangement = Arrangement.spacedBy(
            Spacing.Tight
        )
    ) {
        AppForm(
            value = state.searchQuery,
            onValueChange = onSearchChange,
            placeholder = "Cari nomor transaksi atau nama member",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
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
            minHeight = 48.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                Spacing.Tight
            )
        ) {
            HistoryDateFilterMenu(
                modifier = Modifier.weight(1f),
                state = state,
                onDateFilterChange = onDateFilterChange,
                onStartDateChange = onStartDateChange,
                onEndDateChange = onEndDateChange,
                onApplyCustomFilter = onApplyCustomFilter
            )

            HistoryStatusFilterMenu(
                modifier = Modifier.weight(1f),
                selectedStatus = state.paymentStatus,
                onStatusChange = onPaymentStatusChange
            )
        }
    }
}

@Composable
private fun HistoryDateFilterMenu(
    modifier: Modifier = Modifier,
    state: HistoryUiState,
    onDateFilterChange: (HistoryDateFilter) -> Unit,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onApplyCustomFilter: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    Box(modifier = modifier) {
        AppFilterButton(
            text = state.dateFilter.label,
            icon = Icons.Outlined.CalendarMonth,
            onClick = {
                expanded = true
            },
            modifier = Modifier.width(180.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.width(360.dp)
        ) {
            HistoryDateFilter.entries.forEach { filter ->
                DropdownMenuItem(
                    text = {
                        Text(filter.label)
                    },
                    onClick = {
                        onDateFilterChange(filter)

                        if (filter != HistoryDateFilter.CUSTOM) {
                            expanded = false
                        }
                    },
                    trailingIcon = if (state.dateFilter == filter) {
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

            if (state.dateFilter == HistoryDateFilter.CUSTOM) {
                HorizontalDivider()

                Column(
                    modifier = Modifier.padding(Spacing.Standard),
                    verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
                ) {
                    Text(
                        text = "Rentang tanggal khusus",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            Spacing.Tight
                        )
                    ) {
                        AppForm(
                            value = state.startDate.toDisplayDate(),
                            onValueChange = {},
                            label = "Dari",
                            placeholder = "Pilih tanggal",
                            readOnly = true,
                            onClick = {
                                context.showDatePicker(
                                    initialDate = state.startDate
                                        .toLocalDateOrNull()
                                        ?: LocalDate.now()
                                ) {
                                    onStartDateChange(it.toString())
                                }
                            },
                            minHeight = 48.dp,
                            modifier = Modifier.weight(1f)
                        )

                        AppForm(
                            value = state.endDate.toDisplayDate(),
                            onValueChange = {},
                            label = "Sampai",
                            placeholder = "Pilih tanggal",
                            readOnly = true,
                            onClick = {
                                val startDate =
                                    state.startDate.toLocalDateOrNull()

                                context.showDatePicker(
                                    initialDate = state.endDate
                                        .toLocalDateOrNull()
                                        ?: startDate
                                        ?: LocalDate.now(),
                                    minDate = startDate
                                ) {
                                    onEndDateChange(it.toString())
                                }
                            },
                            minHeight = 48.dp,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                onDateFilterChange(
                                    HistoryDateFilter.TODAY
                                )
                                expanded = false
                            }
                        ) {
                            Text("Reset")
                        }

                        PrimaryButton(
                            text = "Terapkan",
                            onClick = {
                                onApplyCustomFilter()
                                expanded = false
                            },
                            enabled = state.canApplyCustomFilter,
                            fillMaxWidth = false,
                            height = 48.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryStatusFilterMenu(
    modifier: Modifier = Modifier,
    selectedStatus: HistoryPaymentStatus?,
    onStatusChange: (HistoryPaymentStatus?) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        AppFilterButton(
            text = selectedStatus?.label ?: "Semua status",
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
                    Text("Semua status")
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

            HistoryPaymentStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = {
                        Text(status.label)
                    },
                    onClick = {
                        onStatusChange(status)
                        expanded = false
                    },
                    trailingIcon = if (selectedStatus == status) {
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
            }
        }
    }
}