package com.jihan.teeradmin.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmButtonText: String = "Confirm",
    dismissButtonText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() }, confirmButton = {
        TextButton(onClick = onConfirm) {
            Text(confirmButtonText)
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(dismissButtonText)
        }
    }, title = {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
    }, text = {
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
    }, properties = DialogProperties(
        dismissOnBackPress = dismissOnBackPress, dismissOnClickOutside = dismissOnClickOutside
    )
    )
}
