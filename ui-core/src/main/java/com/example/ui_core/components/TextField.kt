package com.example.ui_core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isValueVisible: Boolean,
    onValueVisibilityChange: () -> Unit,
    placeholderText: String,
    isError: Boolean,
    imeAction: ImeAction = ImeAction.Default,
    modifier: Modifier = Modifier
) = BaseTextField(
    value = value,
    onValueChange = onValueChange,
    placeholderText = placeholderText,
    isError = isError,
    trailingIcon = if (isValueVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
    trailingIconDescription = if (isValueVisible) "Hide password" else "Show password",
    onTrailingIconClick = onValueVisibilityChange,
    keyboardType = KeyboardType.Password,
    imeAction = imeAction,
    visualTransformation = if (isValueVisible) VisualTransformation.None else PasswordVisualTransformation(),
    modifier = modifier
)

@Composable
fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    isError: Boolean,
    trailingIcon: ImageVector? = null,
    trailingIconDescription: String? = null,
    onTrailingIconClick: () -> Unit = { },
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) = OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    textStyle = MaterialTheme.typography.bodyLarge,
    colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        errorBorderColor = MaterialTheme.colorScheme.error,
        focusedTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
        errorTextColor = MaterialTheme.colorScheme.onBackground,
        cursorColor = MaterialTheme.colorScheme.onBackground,
        focusedPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
        focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
    ),
    shape = RoundedCornerShape(8.dp),
    placeholder = {
        Text(
            text = placeholderText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    },
    trailingIcon = {
        trailingIcon?.let {
            IconButton(onClick = onTrailingIconClick) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = trailingIconDescription
                )
            }
        }
    },
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
    visualTransformation = visualTransformation,
    singleLine = true,
    isError = isError,
    modifier = modifier.fillMaxWidth()
)