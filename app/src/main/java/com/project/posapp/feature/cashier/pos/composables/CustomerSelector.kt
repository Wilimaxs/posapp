package com.project.posapp.feature.cashier.pos.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.CustomerType
import com.project.posapp.model.PosCustomer
import com.project.posapp.core.theme.Radius
import com.project.posapp.utils.composable.AppSegmentedButton
import com.project.posapp.utils.composable.AppSegmentedOption
import com.project.posapp.utils.composable.PrimaryButton

@Composable
fun CustomerSelector(
    customerType: CustomerType,
    selectedMember: PosCustomer?,
    onCustomerTypeChange: (CustomerType) -> Unit,
    onChooseMember: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .padding(all = Spacing.Standard),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppSegmentedButton(
            modifier = Modifier.widthIn(
                min = 220.dp,
                max = 280.dp
            ),
            options = listOf(
                AppSegmentedOption(
                    value = CustomerType.GUEST,
                    text = "Guest"
                ),
                AppSegmentedOption(
                    value = CustomerType.MEMBER,
                    text = "Member"
                )
            ),
            selected = customerType,
            onSelected = onCustomerTypeChange
        )

        if (customerType == CustomerType.MEMBER) {
            VerticalDivider(
                modifier = Modifier
                    .padding(horizontal = Spacing.Standard)
                    .height(32.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = selectedMember?.name ?: "Harap pilih member",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = selectedMember?.phone ?: "Belum ada member dipilih",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            PrimaryButton(
                text = if (selectedMember == null) {
                    "Pilih member"
                } else {
                    "Ganti member"
                },
                height = 40.dp,
                radius = Radius.Small,
                onClick = onChooseMember,
                reverse = true,
                fillMaxWidth = false
            )
        }
    }
}