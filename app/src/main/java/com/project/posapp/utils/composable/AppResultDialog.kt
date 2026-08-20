package com.project.posapp.utils.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.core.theme.Success

enum class AppResultType {
    SUCCESS,
    ERROR
}

data class AppResultNotification(
    val label: String,
    val value: String
)

@Composable
fun AppResultDialog(
    type: AppResultType,
    title: String,
    message: String,
    primaryButtonText: String,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    notification: AppResultNotification? = null,
    secondaryButtonText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    isLoading: Boolean = false,
    content: (@Composable ColumnScope.() -> Unit)? = null
) {
    val statusColor: Color
    val statusContainerColor: Color
    val statusIcon: ImageVector

    when (type) {
        AppResultType.SUCCESS -> {
            statusColor = Success
            statusContainerColor = Success.copy(alpha = 0.12f)
            statusIcon = Icons.Outlined.CheckCircle
        }

        AppResultType.ERROR -> {
            statusColor = MaterialTheme.colorScheme.error
            statusContainerColor = MaterialTheme.colorScheme.errorContainer
            statusIcon = Icons.Outlined.ErrorOutline
        }
    }

    AppDialog(
        onDismiss = {},
        modifier = modifier,
        widthFraction = 0.38f,
        maxWidth = 520.dp,
        maxHeight = 640.dp,
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Section),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.Large)
        ) {
            ResultStatusIcon(
                icon = statusIcon,
                color = statusColor,
                containerColor = statusContainerColor
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            notification?.let {
                ResultNotification(
                    notification = it,
                    type = type
                )
            }

            content?.let { resultContent ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    content = resultContent
                )
            }

            ResultActions(
                primaryButtonText = primaryButtonText,
                onPrimaryClick = onPrimaryClick,
                secondaryButtonText = secondaryButtonText,
                onSecondaryClick = onSecondaryClick,
                isLoading = isLoading
            )
        }
    }
}

@Composable
private fun ResultStatusIcon(
    icon: ImageVector,
    color: Color,
    containerColor: Color
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .background(
                color = containerColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = color
        )
    }
}

@Composable
private fun ResultNotification(
    notification: AppResultNotification,
    type: AppResultType
) {
    val containerColor = when (type) {
        AppResultType.SUCCESS ->
            MaterialTheme.colorScheme.primary

        AppResultType.ERROR ->
            MaterialTheme.colorScheme.errorContainer
    }

    val contentColor = when (type) {
        AppResultType.SUCCESS ->
            MaterialTheme.colorScheme.onPrimary

        AppResultType.ERROR ->
            MaterialTheme.colorScheme.onErrorContainer
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = containerColor,
                shape = RoundedCornerShape(Radius.Medium)
            )
            .padding(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        Text(
            text = notification.label,
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor
        )

        Text(
            text = notification.value,
            style = MaterialTheme.typography.headlineMedium,
            color = contentColor
        )
    }
}

@Composable
private fun ResultActions(
    primaryButtonText: String,
    onPrimaryClick: () -> Unit,
    secondaryButtonText: String?,
    onSecondaryClick: (() -> Unit)?,
    isLoading: Boolean
) {
    val hasSecondaryButton =
        secondaryButtonText != null &&
                onSecondaryClick != null

    if (hasSecondaryButton) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Standard)
        ) {
            PrimaryButton(
                text = secondaryButtonText,
                onClick = onSecondaryClick,
                modifier = Modifier.weight(1f),
                enabled = !isLoading,
                reverse = true,
                fillMaxWidth = false
            )

            PrimaryButton(
                text = primaryButtonText,
                onClick = onPrimaryClick,
                modifier = Modifier.weight(1f),
                isLoading = isLoading,
                fillMaxWidth = false
            )
        }
    } else {
        PrimaryButton(
            text = primaryButtonText,
            onClick = onPrimaryClick,
            isLoading = isLoading
        )
    }
}