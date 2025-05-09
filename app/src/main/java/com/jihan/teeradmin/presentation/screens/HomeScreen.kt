package com.jihan.teeradmin.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.jihan.composeutils.Cx
import com.jihan.composeutils.toast
import com.jihan.teeradmin.Routes
import com.jihan.teeradmin.data.models.MatchDetail
import com.jihan.teeradmin.domain.viewmodel.MatchViewmodel
import com.jihan.teeradmin.presentation.components.ConfirmationDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewmodel: MatchViewmodel = koinViewModel(), navigate: (Routes) -> Unit
) {
    val context = LocalContext.current

    val scrollState = rememberLazyListState()
    val matchList by viewmodel.matches.collectAsStateWithLifecycle()
    val isLoading by viewmodel.isLoading.collectAsStateWithLifecycle()
    val error by viewmodel.error.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F7FF), Color(0xFFF0F0FF)
                    )
                )
            )
    ) {
        // Background decorative elements
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset((-100).dp, 100.dp)
                .alpha(0.1f)
                .background(
                    color = Color(0xFF7B61FF), shape = CircleShape
                )
                .blur(60.dp)
        )

        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomEnd)
                .offset(50.dp, (-50).dp)
                .alpha(0.1f)
                .background(
                    color = Color(0xFF4CAF50), shape = CircleShape
                )
                .blur(50.dp)
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                isLoading -> {
                    LoadingScreen()
                }

                error != null -> {
                    ErrorScreen(
                        error = error ?: "Unknown error",
                        onRetry = { viewmodel.retryFetchMatches() })
                }

                else -> {
                    // Results List
                    ResultsList(
                        scrollState = scrollState, matchList = matchList.sortedBy { it.id }, onDelete = {
                            viewmodel.deleteMatch(matchId = it, onSuccess = {
                                "Match deleted successfully".toast(context)
                            }, onFailure = {
                                it.toast(context)
                            })
                        }, navigate = navigate
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color(0xFF1976D2), modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading matches...",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ErrorScreen(error: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Lucide.Info,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Error", style = MaterialTheme.typography.titleLarge, color = Color.Red
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
            ) {
                Text("Retry")
            }
        }
    }
}


@Composable
fun ResultsList(
    scrollState: LazyListState,
    matchList: List<MatchDetail>,
    onDelete: (String) -> Unit,
    navigate: (Routes) -> Unit
) {
    // Create staggered animation effect for items
    // Only create animation states when we have matches to show
    val itemAppearingStates = remember(matchList.size) {
        List(matchList.size) {
            mutableStateOf(false)
        }
    }

    // Trigger staggered animation only when we have matches
    LaunchedEffect(matchList.size) {
        if (matchList.isNotEmpty()) {
            matchList.forEachIndexed { index, _ ->
                delay(100L * index)
                itemAppearingStates[index].value = true
            }
        }
    }

    LazyColumn(
        state = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, bottom = 16.dp),
        contentPadding = PaddingValues(bottom = 80.dp) // Space for FAB
    ) {

        if (matchList.isEmpty()) {
            item {
                EmptyStateMessage()
            }
        } else {
            itemsIndexed(matchList) { index, item ->
                AnimatedVisibility(
                    visible = itemAppearingStates[index].value,
                    enter = fadeIn(tween(500)) + slideInVertically(
                        initialOffsetY = { it / 2 }, animationSpec = tween(500)
                    ),
                    exit = fadeOut()
                ) {
                    StylishTeerResultCard(
                        item, navigate = navigate, onDelete = onDelete
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun EmptyStateMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Lucide.Search,
                contentDescription = "No results",
                modifier = Modifier.size(64.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No matches available",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Check back later for new results",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun StylishTeerResultCard(
    match: MatchDetail, onDelete: (String) -> Unit, navigate: (Routes) -> Unit
) {

    var showConfirmationDialog by remember { mutableStateOf(false) }

    if (showConfirmationDialog) {
        ConfirmationDialog(
            title = "Delete Match",
            message = "Are you sure you want to delete this match?",
            onDismiss = { showConfirmationDialog = false },
            onConfirm = {
                showConfirmationDialog = false
                onDelete(match.documentId)
            })
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .shadow(
                elevation = 12.dp, shape = RoundedCornerShape(16.dp), spotColor = Color(0xFF1976D2)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Card Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF1976D2), Color(0xFF42A5F5))
                        ), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(match.id.toString(), modifier = Modifier.align(Alignment.TopStart).padding(10.dp),
                    color = Color.White
                    )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = match.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = match.date, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp
                    )
                }
            }

            // Card Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StylishPlayButton("Edit", clickable = true) {
                        navigate(Routes.EditMatchRoute(match.documentId))
                    }

                    StylishResultTable(match.frNumber, match.srNumber, match.frTime, match.srTime)

                    StylishPlayButton("Delete", clickable = false) {
                        showConfirmationDialog = true
                    }
                }
            }

            Row (Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween){
                Text("FR Opened : ${match.isFrOpened}", color = if (match.isFrOpened) Cx.green600 else Cx.red600 , fontSize = 14.sp, modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold
                    )
                Text("SR Opened : ${match.isSrOpened}", color = if (match.isSrOpened) Cx.green600 else Cx.red600, fontSize = 14.sp, modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold
                    )

            }

        }
    }
}

@Composable
fun StylishPlayButton(round: String, clickable: Boolean, onClick: () -> Unit = {}) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .size(100.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(

                    colors = if (clickable) listOf(Color(0xFF4CAF50), Color(0xFF388E3C))
                    else listOf(Cx.red600, Cx.red500),

                    )
            )
            .clickable {
                scope.launch {
                    onClick()
                }
            }
            .border(
                width = 2.dp, color = Color.White.copy(alpha = 0.3f), shape = CircleShape
            ), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = round, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold
            )
            Text(
                text = "Match", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StylishResultTable(fr: Int?, sr: Int?, frTime: String, srTime: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFDDDDDD), RoundedCornerShape(8.dp))
    ) {
        Column {
            // Header row with solid background
            Row {
                StylishTableCell(
                    text = "FR ($frTime)", isHeader = true, themeColor = Color(0xffd3eafc)
                )
                StylishTableCell(
                    text = "SR ($srTime)", isHeader = true, themeColor = Color(0xffd3eafc)
                )
            }

            // Data row
            Row {
                StylishTableCell(
                    text = fr?.toString() ?: "***", isHeader = false, themeColor = Color(0xffd3eafc)
                )
                StylishTableCell(
                    text = sr?.toString() ?: "***", isHeader = false, themeColor = Color(0xffd3eafc)
                )
            }
        }
    }
}

@Composable
fun StylishTableCell(text: String, isHeader: Boolean, themeColor: Color) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .background(
                if (isHeader) themeColor
                else Color(0xFFFAFAFA)
            )
            .border(0.5.dp, Color(0xFFE0E0E0))
            .padding(vertical = if (isHeader) 8.dp else 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = if (isHeader) 12.sp else 18.sp,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.SemiBold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}




