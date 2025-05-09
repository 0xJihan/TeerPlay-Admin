package com.jihan.teeradmin.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Binary
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Smile
import com.jihan.composeutils.Gap
import com.jihan.teeradmin.domain.viewmodel.CreateMatchViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMatchScreen(
    onBack: () -> Unit, viewModel: CreateMatchViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1F1D2B), Color(0xFF2D2D3F)
                    )
                )
            ).imePadding()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onBack() }) {
                    Icon(
                        imageVector = Lucide.ArrowLeft,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Create New Match",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form Content with beautiful card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF32323E)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title Field
                    CustomTextField(
                        value = viewModel.title.value,
                        onValueChange = { viewModel.title.value = it },
                        label = "Match Title",
                        leadingIcon = {
                            Icon(
                                Lucide.Smile, contentDescription = null, tint = Color(0xFFE0A82E)
                            )
                        })

                    Gap(16)

                    CustomTextField(
                        value = viewModel.matchId.value,
                        onValueChange = {
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) viewModel.matchId.value =
                                it
                        },
                        label = "Match ID",
                        leadingIcon = {
                            Icon(Lucide.Binary,null,tint = Color(0xFFE0A82E))
                        }
                    )

                    Gap(16)

                    // Date Field
                    CustomDateField(
                        value = viewModel.date.value,
                        onValueChange = { viewModel.date.value = it },
                        onDatePickerClick = { showDatePicker = true })

                    Gap(24)

                    // Runners Section
                    Text(
                        text = "Round Details",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Gap(16)

                    val colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFF4F4F60),
                        focusedBorderColor = Color(0xFFE0A82E),
                        unfocusedLabelColor = Color(0xFFAAAAAA),
                        focusedLabelColor = Color(0xFFE0A82E),
                        cursorColor = Color(0xFFE0A82E),
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White
                    )
                    // First Runner Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF3D3D4E)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {


                            Text(
                                text = "First Round",
                                color = Color(0xFFE0A82E),
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Runner Number
                                OutlinedTextField(
                                    value = viewModel.firstRoundNumber.value,
                                    onValueChange = {
                                        if (it.isEmpty() || it.all { char -> char.isDigit() }) viewModel.firstRoundNumber.value =
                                            it
                                    },
                                    label = { Text("Number") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    singleLine = true,
                                    colors =colors,
                                    modifier = Modifier.weight(1f)
                                )

                                // Runner Time
                                OutlinedTextField(
                                    value = viewModel.firstRoundTime.value,
                                    onValueChange = { viewModel.firstRoundTime.value = it },
                                    label = { Text("Time") },
                                    placeholder = {
                                        Text(
                                            "e.g. 3:45 PM", color = Color(0x80FFFFFF)
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next
                                    ),
                                    singleLine = true,
                                    colors = colors ,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Second Runner Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF3D3D4E)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Second Round",
                                color = Color(0xFF64B5F6),
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Runner Number
                                OutlinedTextField(
                                    value = viewModel.secondRoundNumber.value,
                                    onValueChange = {
                                        if (it.isEmpty() || it.all { char -> char.isDigit() }) viewModel.secondRoundNumber.value =
                                            it
                                    },
                                    label = { Text("Number") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                        ),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color(0xFF4F4F60),
                                        focusedBorderColor = Color(0xFF64B5F6),
                                        unfocusedLabelColor = Color(0xFFAAAAAA),
                                        focusedLabelColor = Color(0xFF64B5F6),
                                        cursorColor = Color(0xFF64B5F6),
                                        unfocusedTextColor = Color.White,
                                        focusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                // Runner Time
                                OutlinedTextField(
                                    value = viewModel.secondRoundTime.value,
                                    onValueChange = { viewModel.secondRoundTime.value = it },
                                    label = { Text("Time") },
                                    placeholder = {
                                        Text(
                                            "e.g. 3:42 AM", color = Color(0x80FFFFFF)
                                        )
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color(0xFF4F4F60),
                                        focusedBorderColor = Color(0xFF64B5F6),
                                        unfocusedLabelColor = Color(0xFFAAAAAA),
                                        focusedLabelColor = Color(0xFF64B5F6),
                                        cursorColor = Color(0xFF64B5F6),
                                        unfocusedTextColor = Color.White,
                                        focusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Create Button
            Button(
                onClick = { viewModel.createMatch() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0A82E)
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "CREATE MATCH", fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Date Picker Dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val date = Date(it)
                            viewModel.date.value = dateFormatter.format(date)
                        }
                        showDatePicker = false
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0A82E)
                    )
                ) {
                    Text("Confirm")
                }
            }, dismissButton = {
                OutlinedButton(
                    onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }) {
                DatePicker(
                    state = datePickerState
                )
            }
        }

        // Success Message Overlay
        AnimatedVisibility(
            visible = isSuccess,
            enter = fadeIn(initialAlpha = 0.3f),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x99000000)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(16.dp)
                        .scale(
                            animateFloatAsState(
                                targetValue = if (isSuccess) 1f else 0.8f,
                                animationSpec = tween(300)
                            ).value
                        ), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2A2A36)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Match Created!",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "The match has been successfully created.",
                            color = Color.LightGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Error Message Snackbar
        AnimatedVisibility(
            visible = errorMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            errorMessage?.let {
                Card(
                    shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFB71C1C)
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Lucide.Info,
                            contentDescription = "Error",
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = it, color = Color.White, modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFF4F4F60),
            focusedBorderColor = Color(0xFFE0A82E),
            unfocusedLabelColor = Color(0xFFAAAAAA),
            focusedLabelColor = Color(0xFFE0A82E),
            cursorColor = Color(0xFFE0A82E),
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White
        )
    )
}

@Composable
fun CustomDateField(
    value: String, onValueChange: (String) -> Unit, onDatePickerClick: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Match Date") },
        leadingIcon = {
            Icon(
                Lucide.Calendar, contentDescription = null, tint = Color(0xFFE0A82E)
            )
        },
        trailingIcon = {
            IconButton(onClick = onDatePickerClick) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Select Date",
                    tint = Color(0xFFAAAAAA)
                )
            }
        },
        readOnly = true,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("DD MM YYYY", color = Color(0x80FFFFFF)) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFF4F4F60),
            focusedBorderColor = Color(0xFFE0A82E),
            unfocusedLabelColor = Color(0xFFAAAAAA),
            focusedLabelColor = Color(0xFFE0A82E),
            cursorColor = Color(0xFFE0A82E),
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White
        )
    )
}