package com.jihan.teeradmin.presentation.screens

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Upload
import com.jihan.composeutils.Cx
import com.jihan.composeutils.FileType
import com.jihan.composeutils.Gap
import com.jihan.composeutils.PickerType
import com.jihan.composeutils.rememberCxFilePicker
import com.jihan.composeutils.toast
import com.jihan.teeradmin.domain.uploadImage
import com.jihan.teeradmin.domain.utils.copyToClipboard
import kotlinx.coroutines.launch

@Composable
fun GenerateLinkScreen(

) {


    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var generatedLink by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val imagePicker = rememberCxFilePicker(
        PickerType.Single(
            fileType = FileType.Image
        )
    ){
        imageUri = it.firstOrNull()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Generate Link",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Cx.purple500
        )

        Gap(8)

        SelectionContainer {
        Text(generatedLink,
            modifier = Modifier.clickable{
                generatedLink.copyToClipboard(context)
            }
            )
        }

        Gap(16)
        Card(
            onClick = {
                imagePicker.pick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
            ) {
                if (imageUri != null) {
                    AsyncImage(
                      model = imageUri,
                        contentDescription = "Payment Screenshot",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .border(
                                width = 2.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(16.dp)
                            ), contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Lucide.Upload,
                                contentDescription = null,
                                tint = Cx.purple500,
                                modifier = Modifier.size(64.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Tap to Select Image",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {


            Button(
                onClick = {
                    scope.launch {

                        generatedLink=""
                        isLoading = true
                        if (imageUri==null){
                            imagePicker.pick()
                            return@launch
                        }
                       var url =  uploadImage(context,imageUri!!)
                        if (url!=null){
                            generatedLink = url
                            "Link Generated".toast(context)
                        }else{
                            "Error Generating Link".toast(context)
                        }
                        isLoading = false
                        Log.d("cloudinary", "uploadImage: $url")


                    }
                },
                enabled =imageUri!=null && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor =  Cx.purple500,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {

                if (isLoading)
                    CircularProgressIndicator()
                else
                    Text(
                        text = "Generate Link",
                        fontWeight = FontWeight.Medium
                    )
            }
        }

    }
}