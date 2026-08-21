package com.project.posapp.utils.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing

@Composable
fun AppConfirmationDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    dismissButtonText: String = "Kembali",
    onDismiss: () -> Unit,
    isLoading: Boolean = false,
    isDestructive: Boolean = false,
    errorMessage: String? = null
) {
    AppDialog(
        onDismiss = {
            if (!isLoading) {
                onDismiss()
            }
        },
        modifier = modifier,
        widthFraction = 0.34f,
        maxWidth = 480.dp,
        maxHeight = 360.dp,
        dismissOnBackPress = !isLoading,
        dismissOnClickOutside = !isLoading
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Section),
            verticalArrangement = Arrangement.spacedBy(Spacing.Large)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            errorMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Standard)
            ) {
                PrimaryButton(
                    text = dismissButtonText,
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading,
                    reverse = true,
                    fillMaxWidth = false
                )

                PrimaryButton(
                    text = confirmButtonText,
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    isLoading = isLoading,
                    containerColor = if (isDestructive) {
                        MaterialTheme.colorScheme.error
                    } else {
                        null
                    },
                    fillMaxWidth = false
                )
            }
        }
    }
}