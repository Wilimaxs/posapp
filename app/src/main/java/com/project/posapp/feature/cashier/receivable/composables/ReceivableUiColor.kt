package com.project.posapp.feature.cashier.receivable.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.project.posapp.core.theme.Success
import com.project.posapp.core.theme.Warning

@Composable
internal fun String?.receivableDueStatusColor(): Color =
    when (this?.lowercase()) {
        "overdue" -> MaterialTheme.colorScheme.error
        "today" -> Warning
        else -> Success
    }