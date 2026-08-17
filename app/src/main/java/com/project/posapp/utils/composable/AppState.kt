package com.project.posapp.utils.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing

private enum class AppStateType {
    LOADING,
    EMPTY,
    ERROR
}

@Composable
fun AppState(
    isLoading: Boolean,
    isEmpty: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    loadingTitle: String? = null,
    loadingDescription: String = "Memuat data...",
    emptyTitle: String = "Data tidak ditemukan",
    emptyDescription: String = "Belum ada data untuk ditampilkan.",
    emptyIcon: ImageVector = Icons.Outlined.Inbox,
    errorTitle: String = "Terjadi kesalahan",
    errorIcon: ImageVector = Icons.Outlined.CloudOff,
    actionText: String? = null,
    secondaryActionText: String? = null,
    onAction: (() -> Unit)? = null,
    onSecondaryAction: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    when {
        isLoading -> {
            StateContent(
                type = AppStateType.LOADING,
                modifier = modifier,
                title = loadingTitle,
                description = loadingDescription
            )
        }

        errorMessage != null -> {
            StateContent(
                type = AppStateType.ERROR,
                modifier = modifier,
                title = errorTitle,
                description = errorMessage,
                icon = errorIcon,
                actionText = actionText ?: if (onAction != null) {
                    "Coba lagi"
                } else {
                    null
                },
                secondaryActionText = secondaryActionText,
                onAction = onAction,
                onSecondaryAction = onSecondaryAction
            )
        }

        isEmpty -> {
            StateContent(
                type = AppStateType.EMPTY,
                modifier = modifier,
                title = emptyTitle,
                description = emptyDescription,
                icon = emptyIcon,
                actionText = actionText,
                secondaryActionText = secondaryActionText,
                onAction = onAction,
                onSecondaryAction = onSecondaryAction
            )
        }

        else -> {
            content()
        }
    }
}

@Composable
private fun StateContent(
    type: AppStateType,
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String,
    icon: ImageVector? = null,
    actionText: String? = null,
    secondaryActionText: String? = null,
    onAction: (() -> Unit)? = null,
    onSecondaryAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = Spacing.Standard,
            alignment = Alignment.CenterVertically
        )
    ) {
        when (type) {
            AppStateType.LOADING -> {
                CircularProgressIndicator()
            }

            AppStateType.EMPTY,
            AppStateType.ERROR -> {
                StateIcon(
                    type = type,
                    icon = requireNotNull(icon)
                )
            }
        }

        title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (
            onAction != null ||
            onSecondaryAction != null
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    Spacing.Tight
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (
                    onSecondaryAction != null &&
                    secondaryActionText != null
                ) {
                    PrimaryButton(
                        text = secondaryActionText,
                        onClick = onSecondaryAction,
                        reverse = true,
                        fillMaxWidth = false
                    )
                }

                if (
                    onAction != null &&
                    actionText != null
                ) {
                    PrimaryButton(
                        text = actionText,
                        onClick = onAction,
                        fillMaxWidth = false
                    )
                }
            }
        }
    }
}

@Composable
private fun StateIcon(
    type: AppStateType,
    icon: ImageVector
) {
    val backgroundColor = when (type) {
        AppStateType.ERROR ->
            MaterialTheme.colorScheme.errorContainer

        else ->
            MaterialTheme.colorScheme.surfaceContainerHigh
    }

    val iconColor = when (type) {
        AppStateType.ERROR ->
            MaterialTheme.colorScheme.error

        else ->
            MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(44.dp),
            tint = iconColor
        )
    }
}