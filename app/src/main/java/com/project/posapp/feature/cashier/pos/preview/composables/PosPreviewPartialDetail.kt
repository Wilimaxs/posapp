package com.project.posapp.feature.cashier.pos.preview.composables

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.core.theme.Radius
import com.project.posapp.utils.extensions.toRupiah
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PosPreviewPartialDetail(
    total: Long,
    downPayment: String,
    dueDate: String?,
    remainingReceivable: Long,
    onDownPaymentChange: (String) -> Unit,
    onDueDateChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
    ) {
        Text(
            text = "Rincian pembayaran sebagian",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Standard)
        ) {
            DownPaymentField(
                value = downPayment,
                onValueChange = onDownPaymentChange,
                modifier = Modifier.weight(1f)
            )
            DueDateField(
                value = dueDate,
                onValueChange = onDueDateChange,
                modifier = Modifier.weight(1f)
            )
        }
        PartialSummary(
            total = total,
            downPayment = downPayment.toLongOrNull() ?: 0L,
            remainingReceivable = remainingReceivable,
            dueDate = dueDate
        )
    }
}

@Composable
private fun DownPaymentField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        Text(
            text = "Uang muka",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = value.formatAmount(),
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Text(
                    text = "Rp",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            shape = RoundedCornerShape(
                size = Radius.Medium
            )
        )

        Text(
            text = "Nominal yang dibayarkan sekarang.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DueDateField(
    value: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            Spacing.Tight
        )
    ) {
        Text(
            text = "Tanggal jatuh tempo",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = value.toDisplayDate(),
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            singleLine = true,
            placeholder = {
                Text(text = "Pilih tanggal")
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        val selectedDate = value
                            ?.let {
                                runCatching {
                                    LocalDate.parse(it)
                                }.getOrNull()
                            }
                            ?: LocalDate.now()

                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                val date = LocalDate.of(
                                    year,
                                    month + 1,
                                    day
                                )

                                onValueChange(
                                    date.toString()
                                )
                            },
                            selectedDate.year,
                            selectedDate.monthValue - 1,
                            selectedDate.dayOfMonth
                        ).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = "Pilih tanggal jatuh tempo"
                    )
                }
            },
            textStyle = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            ),
            shape = RoundedCornerShape(
                size = Radius.Medium
            )
        )

        Text(
            text = "Batas waktu pelunasan piutang.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PartialSummary(
    total: Long,
    downPayment: Long,
    remainingReceivable: Long,
    dueDate: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(
                    size = Radius.Medium
                )
            )
            .padding(all = Spacing.Standard),
        verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        DetailRow(
            label = "Total transaksi",
            value = total.toRupiah()
        )

        DetailRow(
            label = "Uang muka",
            value = downPayment.toRupiah()
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        DetailRow(
            label = "Sisa piutang",
            value = remainingReceivable.toRupiah(),
            emphasize = true
        )

        DetailRow(
            label = "Jatuh tempo",
            value = dueDate.toLongDisplayDate()
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    emphasize: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (emphasize) {
                MaterialTheme.typography.labelLarge
            } else {
                MaterialTheme.typography.bodyMedium
            },
            color = if (emphasize) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )

        Text(
            text = value,
            style = if (emphasize) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.bodyMedium
            },
            color = if (emphasize) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

private fun String.formatAmount(): String {
    if (isBlank()) {
        return ""
    }

    return toLongOrNull()
        ?.let {
            NumberFormat
                .getNumberInstance(
                    Locale.forLanguageTag("id-ID")
                )
                .format(it)
        }
        ?: ""
}

private fun String?.toDisplayDate(): String {
    if (this == null) {
        return ""
    }

    return runCatching {
        LocalDate
            .parse(this)
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }.getOrDefault("")
}

private fun String?.toLongDisplayDate(): String {
    if (this == null) {
        return "-"
    }

    return runCatching {
        LocalDate
            .parse(this)
            .format(
                DateTimeFormatter.ofPattern(
                    "dd MMMM yyyy",
                    Locale.forLanguageTag("id-ID")
                )
            )
    }.getOrDefault("-")
}