package com.project.posapp.utils.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing

data class AppSegmentedOption<T>(
    val value: T,
    val text: String,
    val enabled: Boolean = true
)

@Composable
fun <T> AppSegmentedButton(
    options: List<AppSegmentedOption<T>>,
    selected: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 40.dp,
    radius: Dp = Radius.Medium,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(size = radius)
            )
            .padding(all = Spacing.Micro),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        options.forEach { option ->
            val isSelected = selected == option.value

            PrimaryButton(
                text = option.text,
                onClick = {
                    onSelected(option.value)
                },
                enabled = option.enabled,
                modifier = Modifier.weight(1f),
                fillMaxWidth = false,
                height = height,
                radius = radius,
                textStyle = textStyle,
                containerColor = if (isSelected) {
                    MaterialTheme.colorScheme.surface
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHigh
                },
                contentColor = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                borderColor = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                borderWidth = 1.dp
            )
        }
    }
}