package com.jihan.teeradmin.presentation.components
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults // Import Material3's AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jihan.composeutils.Gap

/**
 * Displays a dialog to inform the user that they have been banned.
 *
 * @param onDismissRequest Called when the user clicks the dismiss button.  If null, no dismiss
 * button is shown.
 * @param title The title of the dialog. Defaults to "Account Banned".
 * @param text The message to display to the user. Defaults to "Your account has been banned from using this app."
 */
@Composable
fun BanDialog(
    onDismissRequest: (() -> Unit)?,
    onLogout: (() -> Unit)?, // Added onLogout parameter
    title: String = "Account Suspended",
    text: String = "Your account has been suspended by an administrator. You no longer have access to this application.",
) {
    AlertDialog(
        onDismissRequest = {
        },
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 20.sp,
                    color = Color.Red, // Make the title red to indicate a serious issue
                    textAlign = TextAlign.Center, // Center the title
                ),
                modifier = Modifier.padding(bottom = 8.dp), // Add some padding below the title
            )
        },
        text = {
            Column {

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
            )

                Gap(20)
            Text(
                text = "Please contact the system administrator for more information.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
            )
            }
        },
        // Use a custom shape for the dialog
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        containerColor = Color.DarkGray, // Set a dark background for the dialog.  Replaces backgroundColor
        titleContentColor = AlertDialogDefaults.titleContentColor,
        textContentColor = AlertDialogDefaults.textContentColor,

        // Use confirmButton and dismissButton
        confirmButton = {
            if (onDismissRequest != null) {
                Button(
                    onClick = { onDismissRequest() },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                ) {
                    Text("Exit")
                }
            }
        },
        dismissButton = {
            if (onLogout != null) {
                Button(
                    onClick = { onLogout() },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                ) {
                    Text("Log Out")
                }
            }
        },
    )
}