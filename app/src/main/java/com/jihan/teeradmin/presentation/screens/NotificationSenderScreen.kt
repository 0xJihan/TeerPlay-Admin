package com.jihan.teeradmin.presentation.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.jihan.composeutils.CxButton
import com.jihan.composeutils.CxEditText
import com.jihan.composeutils.Gap
import com.jihan.composeutils.isEmpty
import com.jihan.composeutils.toast
import com.jihan.teeradmin.domain.sendCustomNotificationToTopic
import com.jihan.teeradmin.presentation.components.ScrollView

@Composable
fun NotificationSenderScreen() {


    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }

    var selectedPosition by remember { mutableIntStateOf(0) }

    Scaffold(Modifier.fillMaxSize()) {
        ScrollView(
            Modifier
                .fillMaxSize()
                .padding(it), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Gap(20)
            Text("Notification Sender",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Gap(20)

            CxEditText(
                value = title, label = "Notification Title", colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ), maxLines = 10, imeAction = ImeAction.Default,
                shape = CircleShape
            ) { title = it }

            Gap(15)
            CxEditText(
                message, "Notification Message", colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ), maxLines = 10, imeAction = ImeAction.Default,
                shape = CircleShape
            ) { message = it }
            Gap(15)
            CxEditText(
                image, "Notification Image Link", colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ), maxLines = 4, imeAction = ImeAction.Default,
                shape = CircleShape
            ) {
                image = it
                if (it.isEmpty) {
                    selectedPosition = 0
                }
            }
            Gap(25)



            AnimatedVisibility(image.isNotEmpty()) {

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilterChip(
                        selected = selectedPosition == 1,
                        onClick = { selectedPosition = 1 },
                        label = { Text("Big Image") })
                    Gap(20)
                    FilterChip(
                        selected = selectedPosition == 2,
                        onClick = { selectedPosition = 2 },
                        label = { Text("Small Image") })
                }
            }


            CxButton(
                "Send Notification",
                enabled = title.isNotEmpty() && message.isNotEmpty(),
            ) {

                sendCustomNotificationToTopic(
                    topic = "user", title = title.trim(), message = message.trim(), customizer = {

                        it.apply {
                            if (selectedPosition == 1) {
                                put("big_picture", image.trim())
                            } else if (selectedPosition == 2) {
                                put("large_icon", image.trim())
                            }
                        }

                    },
                    onSuccess = {
                        Handler(Looper.getMainLooper()).post {
                            it.toast(context)
                        }
                    }
                )

                image=""
                title=""
                message=""
            }


        }
    }

}