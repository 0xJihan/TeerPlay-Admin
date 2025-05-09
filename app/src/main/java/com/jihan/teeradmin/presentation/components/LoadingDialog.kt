package com.jihan.teeradmin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.jihan.composeutils.Gap

@Composable
fun LoadingDialog(loading: Boolean, text: String = "Loading...") {

    if (!loading) return
    Box(
        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {

        Column(Modifier.background(Color.Transparent)) {
            CircularProgressIndicator(
                strokeWidth = 4.dp,

            )
            Gap(10)
            Text(text)
        }

    }

}