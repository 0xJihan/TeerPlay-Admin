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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Binary
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Save
import com.composables.icons.lucide.Smile
import com.jihan.composeutils.CenterBox
import com.jihan.composeutils.Gap
import com.jihan.composeutils.isNumber
import com.jihan.composeutils.text
import com.jihan.composeutils.toast
import com.jihan.teeradmin.Routes
import com.jihan.teeradmin.data.models.MatchDetail
import com.jihan.teeradmin.domain.viewmodel.EditMatchViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMatchScreen(
    navigate: (Routes?) -> Unit, matchDetail: MatchDetail?
) {


    if(matchDetail==null){
        CenterBox {
            "Invalid Match ID".text.size(25).make()
        }
        return
    }



    val context = LocalContext.current
    var title by remember { mutableStateOf(matchDetail.title) }
    var id by remember { mutableStateOf(matchDetail.id.toString()) }
    var date by remember { mutableStateOf(matchDetail.date) }
    var frNumber by remember { mutableStateOf(matchDetail.frNumber?.toString() ?: "") }
    var srNumber by remember { mutableStateOf(matchDetail.srNumber?.toString() ?: "") }
    var frTime by remember { mutableStateOf(matchDetail.frTime) }
    var srTime by remember { mutableStateOf(matchDetail.srTime) }
    var isFrOpened by remember { mutableStateOf(matchDetail.isFrOpened) }
    var isSrOpened by remember { mutableStateOf(matchDetail.isFrOpened) }

    var numberMultiplier by remember { mutableStateOf(matchDetail.numberMultiplier.toString()) }
    var homeMultiplier by remember { mutableStateOf(matchDetail.homeMultiplier.toString()) }

    val viewModel = koinViewModel<EditMatchViewModel>()

    val isLoading by viewModel.isLoading.collectAsState()
    val isInitialLoading by viewModel.isInitialLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
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
            )
            .statusBarsPadding()
            .imePadding()
    ) {
        if (isInitialLoading) {
            // Loading state
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFE0A82E), strokeWidth = 3.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Loading match details...", color = Color.White, fontSize = 16.sp
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Section with Edit Match title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navigate(null) }) {
                        Icon(
                            imageVector = Lucide.ArrowLeft,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Edit Match",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Delete button
                    IconButton(
                        onClick = { showDeleteConfirmation = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Match",
                            tint = Color(0xFFFF5252)
                        )
                    }
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
                            value = title,
                            onValueChange = { title = it },
                            label = "Match Title",
                            leadingIcon = {
                                Icon(
                                    Lucide.Smile,
                                    contentDescription = null,
                                    tint = Color(0xFFE0A82E)
                                )
                            })

                        Gap(16)

                        CustomTextField(
                            value = id,
                            onValueChange = { id = it },
                            label = "Match ID",
                            leadingIcon = {
                                Icon(
                                    Lucide.Binary,
                                    contentDescription = null,
                                    tint = Color(0xFFE0A82E)
                                )
                            })
                        Gap(16)
                        CustomTextField(
                            value = numberMultiplier,
                            onValueChange = { numberMultiplier = it },
                            label = "Number Multiplier"
                          )

                        Gap(16)
                        CustomTextField(
                            value = homeMultiplier,
                            onValueChange = { homeMultiplier = it },
                            label = "House & Ending Multiplier"
                          )

                        Gap(16)

                        // Date Field
                        CustomDateField(
                            value = date,
                            onValueChange = { date = it },
                            onDatePickerClick = { showDatePicker = true })

                       Gap(24)
                        // First Round Status Switch
                        SwitchToggle(
                            text = "First Round Status",
                            isOpened = isFrOpened,
                            onCheckedChange = { isFrOpened = it }
                        )

                        Gap(24)

                        // Second Round Status Switch
                        SwitchToggle(
                            text = "Second Round Status",
                            isOpened = isSrOpened,
                            onCheckedChange = { isSrOpened = it }
                        )
                       Gap(24)

                        // Round Section
                        Text(
                            text = "Round Details",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // First Round Section
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
                                    // Round Number
                                    OutlinedTextField(
                                        value = frNumber,
                                        onValueChange = {
                                            if (it.isEmpty() || it.all { char -> char.isDigit() }) frNumber =
                                                it
                                        },
                                        label = { Text("Number") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = Color(0xFF4F4F60),
                                            focusedBorderColor = Color(0xFFE0A82E),
                                            unfocusedLabelColor = Color(0xFFAAAAAA),
                                            focusedLabelColor = Color(0xFFE0A82E),
                                            cursorColor = Color(0xFFE0A82E),
                                            unfocusedTextColor = Color.White,
                                            focusedTextColor = Color.White
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )

                                    // Round Time
                                    OutlinedTextField(
                                        value = frTime,
                                        onValueChange = { frTime = it },
                                        label = { Text("Time") },
                                        placeholder = {
                                            Text(
                                                "e.g. 3:45 PM", color = Color(0x80FFFFFF)
                                            )
                                        },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = Color(0xFF4F4F60),
                                            focusedBorderColor = Color(0xFFE0A82E),
                                            unfocusedLabelColor = Color(0xFFAAAAAA),
                                            focusedLabelColor = Color(0xFFE0A82E),
                                            cursorColor = Color(0xFFE0A82E),
                                            unfocusedTextColor = Color.White,
                                            focusedTextColor = Color.White
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Second Round Section
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
                                    // Round Number
                                    OutlinedTextField(
                                        value = srNumber,
                                        onValueChange = {
                                            if (it.isEmpty() || it.all { char -> char.isDigit() }) srNumber =
                                                it
                                        },
                                        label = { Text("Number") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

                                    // Round Time
                                    OutlinedTextField(
                                        value = srTime,
                                        onValueChange = { srTime = it },
                                        label = { Text("Time") },
                                        placeholder = {
                                            Text(
                                                "e.g. 3:42.88", color = Color(0x80FFFFFF)
                                            )
                                        },
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
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                val isButtonEnabled = id.isNumber() && title.isNotEmpty() && date.isNotEmpty() && frTime.isNotEmpty() && srTime.isNotEmpty()


                //! Update Button
                Button(
                    onClick = {

                        if (numberMultiplier.toDoubleOrNull()==null || homeMultiplier.toDoubleOrNull()==null){
                            "Invalid Multiplier".toast(context)
                            return@Button
                        }


                        val matchInfo = mapOf<String, Any?>(
                            "id" to id.toIntOrNull(),
                            "title" to title,
                            "date" to date,
                            "frNumber" to frNumber.toIntOrNull(),
                            "srNumber" to srNumber.toIntOrNull(),
                            "frTime" to frTime,
                            "srTime" to srTime,
                            "isFrOpened" to isFrOpened,
                            "isSrOpened" to isSrOpened ,
                            "numberMultiplier" to numberMultiplier.toDouble(),
                            "homeMultiplier" to homeMultiplier.toDouble(),


                        )




                        viewModel.updateMatch(
                            matchId = matchDetail.documentId, matchInfo
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7E57C2)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading && isButtonEnabled
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Lucide.Save, contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "UPDATE MATCH",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // Date Picker Dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val mDate = Date(it)
                            date = dateFormatter.format(mDate)
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

        // Delete Confirmation Dialog
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = {
                    Text("Delete Match")
                },
                text = {
                    Text("Are you sure you want to delete this match? This action cannot be undone.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteMatch(matchId = matchDetail.documentId) {
                                showDeleteConfirmation = false
                                navigate(null)
                            }
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF5252)
                        )
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDeleteConfirmation = false }) {
                        Text("Cancel")
                    }
                },
                containerColor = Color(0xFF32323E),
                titleContentColor = Color.White,
                textContentColor = Color.LightGray
            )
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
                                .background(Color(0xFF7E57C2).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = Color(0xFF7E57C2),
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Match Updated!",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "The match has been successfully updated.",
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
fun SwitchToggle(
    text: String,
    isOpened: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isOpened) "Open" else "Closed",
                color = if (isOpened) Color(0xFF4CAF50) else Color(
                    0xFFFF5252
                ),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Switch(
                checked = isOpened,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF4CAF50),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFFF5252).copy(alpha = 0.6f)
                )
            )
        }
    }
}
