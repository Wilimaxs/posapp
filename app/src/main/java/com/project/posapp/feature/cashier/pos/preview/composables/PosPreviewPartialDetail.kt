package com.project.posapp.feature.cashier.pos.preview.composables

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.utils.composable.AppForm
import com.project.posapp.utils.extensions.formatAmount
import com.project.posapp.utils.extensions.showDatePicker
import com.project.posapp.utils.extensions.toDisplayDate
import com.project.posapp.utils.extensions.toLocalDateOrNull
import com.project.posapp.utils.extensions.toLongDisplayDate
import com.project.posapp.utils.extensions.toRupiah
import java.time.LocalDate

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
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
    ) {
        Text(
            text = "Rincian Pembayaran Sebagian",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Standard)
        ) {
            AppForm(
                value = downPayment.formatAmount(),
                onValueChange = onDownPaymentChange,
                modifier = Modifier.weight(1f),
                label = "Uang muka",
                prefixText = "Rp",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                minHeight = 64.dp
            )

            AppForm(
                value = dueDate.toDisplayDate(),
                onValueChange = {},
                modifier = Modifier.weight(1f),
                label = "Tanggal jatuh tempo",
                placeholder = "Pilih tanggal",
                readOnly = true,
                onClick = {
                    context.showDatePicker(
                        initialDate = dueDate.toLocalDateOrNull()
                            ?: LocalDate.now()
                    ) { date ->
                        onDueDateChange(
                            date.toString()
                        )
                    }
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = null
                    )
                },
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                minHeight = 64.dp
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
                shape = RoundedCornerShape(Radius.Medium)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Radius.Medium)
            )
            .padding(Spacing.Large),
        verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
    ) {
        DetailRow(
            label = "Total transaksi",
            value = total.toRupiah()
        )

        DetailRow(
            label = "Uang muka",
            value = downPayment.toRupiah()
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )

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
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.bodyLarge
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
                MaterialTheme.typography.titleLarge
            } else {
                MaterialTheme.typography.bodyLarge
            },
            color = if (emphasize) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}