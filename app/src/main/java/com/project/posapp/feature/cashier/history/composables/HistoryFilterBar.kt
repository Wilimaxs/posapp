package com.project.posapp.feature.cashier.history.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.project.posapp.utils.composable.AppFilterOption
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

    val options: List<AppFilterOption<HistoryPaymentStatus?>> =
        listOf(
            AppFilterOption<HistoryPaymentStatus?>(
                value = null,
                text = "Semua status"
            )
        ) + HistoryPaymentStatus.entries.map { status ->
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

            AppFilterButton(
                modifier = Modifier.weight(1f),
                text = state.paymentStatus?.label ?: "Semua status",
                options = options,
                selected = state.paymentStatus,
                onSelected = onPaymentStatusChange,
            )
        }
    }
}

@Composable
private fun HistoryDateFilterMenu(
    state: HistoryUiState,
    onDateFilterChange: (HistoryDateFilter) -> Unit,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onApplyCustomFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AppFilterButton(
        text = state.dateFilter.label,
        icon = Icons.Outlined.CalendarMonth,
        options = HistoryDateFilter.entries.map { filter ->
            AppFilterOption(
                value = filter,
                text = filter.label,
                dismissOnSelect = filter != HistoryDateFilter.CUSTOM
            )
        },
        selected = state.dateFilter,
        onSelected = onDateFilterChange,
        modifier = modifier,
        dropdownModifier = Modifier.width(360.dp),
        customDropdownContent = { dismiss ->
            if (state.dateFilter == HistoryDateFilter.CUSTOM) {
                HorizontalDivider()

                Column(
                    modifier = Modifier.padding(all = Spacing.Standard),
                    verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
                ) {
                    Text(
                        text = "Rentang tanggal khusus",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.Tight)
                    ) {
                        AppForm(
                            value = state.startDate.toDisplayDate(),
                            onValueChange = {},
                            label = "Dari",
                            placeholder = "Pilih tanggal",
                            readOnly = true,
                            onClick = {
                                context.showDatePicker(
                                    initialDate = state.startDate.toLocalDateOrNull() ?: LocalDate.now()
                                ) {
                                    onStartDateChange(
                                        it.toString()
                                    )
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
                                val startDate = state.startDate.toLocalDateOrNull()

                                context.showDatePicker(
                                    initialDate = state.endDate.toLocalDateOrNull() ?: startDate ?: LocalDate.now(),
                                    minDate = startDate
                                ) {
                                    onEndDateChange(
                                        it.toString()
                                    )
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
                                dismiss()
                            }
                        ) {
                            Text(
                                text = "Reset"
                            )
                        }

                        PrimaryButton(
                            text = "Terapkan",
                            onClick = {
                                onApplyCustomFilter()
                                dismiss()
                            },
                            enabled = state.canApplyCustomFilter,
                            fillMaxWidth = false,
                            height = 48.dp
                        )
                    }
                }
            }
        }
    )
}