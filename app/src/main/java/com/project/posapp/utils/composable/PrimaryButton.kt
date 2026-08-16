package com.project.posapp.utils.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.ui.theme.Radius

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    reverse: Boolean = false,
    isLoading: Boolean = false,
    height: Dp = 56.dp,
    icon: ImageVector? = null,
    contentColor: Color? = null,
    containerColor: Color? = null,
    borderColor: Color? = null,
    borderWidth: Dp = 1.dp,
    radius: Dp = Radius.Medium,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    fillMaxWidth: Boolean = true,
    content: (@Composable RowScope.() -> Unit)? = null
) {
    val primary = containerColor ?: MaterialTheme.colorScheme.primary

    val resolvedContentColor = contentColor ?: if (reverse) {
        primary
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    val buttonModifier = if (fillMaxWidth) {
        modifier
            .fillMaxWidth()
            .height(height)
    } else {
        modifier.height(height)
    }

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = buttonModifier,
        shape = RoundedCornerShape(radius),
        border = if (reverse) {
            BorderStroke(
                width = borderWidth,
                color = borderColor ?: primary
            )
        } else if (borderColor != null) {
            BorderStroke(
                width = borderWidth,
                color = borderColor
            )
        } else {
            null
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (reverse) {
                Color.Transparent
            } else {
                primary
            },
            contentColor = resolvedContentColor,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = resolvedContentColor
                )
            }

            content != null -> {
                content()
            }

            else -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Tight)
                ) {
                    icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = text,
                        style = textStyle
                    )
                }
            }
        }
    }
}