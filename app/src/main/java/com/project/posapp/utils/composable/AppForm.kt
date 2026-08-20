package com.project.posapp.utils.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing

@Composable
fun AppForm(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,

    label: String? = null,
    labelHelper: String? = null,
    placeholder: String = "",

    required: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,

    isPassword: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else 5,
    maxLength: Int? = null,

    prefixText: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,

    errorMessage: String? = null,

    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,

    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    minHeight: Dp = 56.dp
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        if (label != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.Micro)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge
                )

                if (required) {
                    Text(
                        text = "*",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                labelHelper?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    if (
                        maxLength == null ||
                        newValue.length <= maxLength
                    ) {
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(
                        minHeight = minHeight
                    ),
                enabled = enabled,
                readOnly = readOnly,
                singleLine = singleLine,
                minLines = minLines,
                maxLines = maxLines,
                textStyle = textStyle,
                isError = errorMessage != null,
                placeholder = {
                    if (placeholder.isNotBlank()) {
                        Text(
                            text = placeholder
                        )
                    }
                },
                prefix = prefixText?.let { text ->
                    {
                        Text(text = text)
                    }
                },
                leadingIcon = leadingIcon,
                trailingIcon = when {
                    isPassword -> {
                        {
                            IconButton(
                                onClick = {
                                    passwordVisible = !passwordVisible
                                }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) {
                                        Icons.Outlined.VisibilityOff
                                    } else {
                                        Icons.Outlined.Visibility
                                    },
                                    contentDescription = if (passwordVisible) {
                                        "Sembunyikan password"
                                    } else {
                                        "Tampilkan password"
                                    }
                                )
                            }
                        }
                    }

                    trailingIcon != null -> trailingIcon

                    else -> null
                },
                visualTransformation = if (
                    isPassword &&
                    !passwordVisible
                ) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                supportingText = errorMessage?.let { message ->
                    {
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                    disabledContainerColor =
                        MaterialTheme.colorScheme.surfaceContainerHigh
                )
            )

            if (
                enabled &&
                readOnly &&
                onClick != null
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            onClick = onClick
                        )
                )
            }
        }
    }
}