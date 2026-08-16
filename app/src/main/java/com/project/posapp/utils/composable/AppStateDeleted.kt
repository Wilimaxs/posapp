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

enum class AppStateTypeDeleted {
    LOADING,
    EMPTY,
    ERROR
}

//TODO: delete
@Composable
fun AppStateDeleted(
    type: AppStateTypeDeleted,
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    icon: ImageVector? = null,
    actionText: String? = null,
    secondaryActionText: String? = null,
    onAction: (() -> Unit)? = null,
    onSecondaryAction: (() -> Unit)? = null
) {
    val resolvedTitle = when (type) {
        AppStateTypeDeleted.LOADING -> title
        AppStateTypeDeleted.EMPTY -> title ?: "Data tidak ditemukan"
        AppStateTypeDeleted.ERROR -> title ?: "Terjadi kesalahan"
    }

    val resolvedDescription = when (type) {
        AppStateTypeDeleted.LOADING -> description ?: "Memuat data..."
        AppStateTypeDeleted.EMPTY -> description ?: "Belum ada data untuk ditampilkan."
        AppStateTypeDeleted.ERROR -> description ?: "Terjadi kesalahan saat memuat data."
    }

    val resolvedIcon = when (type) {
        AppStateTypeDeleted.LOADING -> null
        AppStateTypeDeleted.EMPTY -> icon ?: Icons.Outlined.Inbox
        AppStateTypeDeleted.ERROR -> icon ?: Icons.Outlined.CloudOff
    }

    val resolvedActionText = when (type) {
        AppStateTypeDeleted.ERROR ->
            actionText ?: if (onAction != null) {
                "Coba lagi"
            } else {
                null
            }

        else ->
            actionText
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
    ) {
        when (type) {
            AppStateTypeDeleted.LOADING -> {
                CircularProgressIndicator()
            }

            AppStateTypeDeleted.EMPTY,
            AppStateTypeDeleted.ERROR -> {
                StateIcon(
                    type = type,
                    icon = requireNotNull(resolvedIcon)
                )
            }
        }

        resolvedTitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = resolvedDescription,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (onAction != null ||
            onSecondaryAction != null
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.Tight),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onSecondaryAction != null && secondaryActionText != null) {
                    PrimaryButton(
                        text = secondaryActionText,
                        onClick = onSecondaryAction,
                        reverse = true,
                        fillMaxWidth = false
                    )
                }

                if (onAction != null && resolvedActionText != null) {
                    PrimaryButton(
                        text = resolvedActionText,
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
    type: AppStateTypeDeleted,
    icon: ImageVector
) {
    val backgroundColor = when (type) {
        AppStateTypeDeleted.ERROR -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surfaceContainerHigh
    }

    val iconColor = when (type) {
        AppStateTypeDeleted.ERROR -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
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