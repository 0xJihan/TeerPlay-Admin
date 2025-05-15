package com.jihan.teeradmin.presentation.components

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jihan.composeutils.CenterBox
import com.jihan.composeutils.Gap

@Composable
fun EmptyBox(
    title: String,
    description: String,
    @RawRes lottieRes: Int,

    ) {
    CenterBox {

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieRes))

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(250.dp).fillMaxWidth()

        )
        Text(title,
            style = MaterialTheme.typography.headlineSmall,)
        Gap(10)
        Text(description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant)


    }

}