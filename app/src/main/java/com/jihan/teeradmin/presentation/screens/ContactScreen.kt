package com.jihan.teeradmin.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jihan.teeradmin.R
import com.jihan.composeutils.Gap
import com.jihan.composeutils.toast
import com.jihan.lucide_icons.lucide
import com.jihan.teeradmin.domain.utils.painter
import com.jihan.teeradmin.domain.viewmodel.FirebaseViewmodel
import com.jihan.teeradmin.presentation.components.LoadingDialog
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    viewmodel: FirebaseViewmodel = koinViewModel(),
) {

    val context = LocalContext.current
    val appInfo by viewmodel.appInfo.collectAsStateWithLifecycle()


    val scrollState = rememberScrollState()

    // State for payment numbers

    var bkashNumber by remember { mutableStateOf(appInfo.bkashNumber ) }
    var nagadNumber by remember { mutableStateOf(appInfo.nagadNumber ) }
    var rocketNumber by remember { mutableStateOf(appInfo.rocketNumber ) }
    var indianQr by remember { mutableStateOf(appInfo.indianQr ) }

    //! other info

    var googlePayNumber by remember { mutableStateOf(appInfo.googlePayNumber) }
    var phonePeNumber by remember { mutableStateOf(appInfo.phonePeNumber) }
    var paytmNumber by remember { mutableStateOf(appInfo.paytmNumber) }

// State for social media links
    var telegramLink by remember { mutableStateOf(appInfo.telegramLink ) }

    var isLoading by remember { mutableStateOf(false) }


    LaunchedEffect(appInfo) {
        appInfo.let { info ->
            bkashNumber = info.bkashNumber
            nagadNumber = info.nagadNumber
            rocketNumber = info.rocketNumber

            telegramLink = info.telegramLink

        }
    }


    Surface(
        modifier = Modifier.fillMaxSize().imePadding(), color = MaterialTheme.colorScheme.background
    ) {

        Box(Modifier.fillMaxSize()){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Payment and Contact", style = MaterialTheme.typography.headlineMedium
            )

            // Payment Methods Section
            Card(
                modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Payment Methods", style = MaterialTheme.typography.titleMedium
                    )

                    PaymentTextField(
                        value = bkashNumber,
                        onValueChange = { bkashNumber = it },
                        label = "bKash Number",
                        icon = R.drawable.bkash
                    )

                    PaymentTextField(
                        value = nagadNumber,
                        onValueChange = { nagadNumber = it },
                        label = "Nagad Number",
                        icon = R.drawable.nagad
                    )

                    PaymentTextField(
                        value = rocketNumber,
                        onValueChange = { rocketNumber = it },
                        label = "Rocket Number",
                        icon = R.drawable.rocket,
                    )


                    PaymentTextField(
                        value = googlePayNumber,
                        onValueChange = { googlePayNumber = it },
                        label = "Google Pay",
                        icon = R.drawable.google_pay,
                    )


                    PaymentTextField(
                        value = phonePeNumber,
                        onValueChange = { phonePeNumber = it },
                        label = "PhonePe Number",
                        icon = R.drawable.phonepe,
                    )


                    PaymentTextField(
                        value = paytmNumber,
                        onValueChange = { paytmNumber = it },
                        label = "Paytm Number",
                        icon = R.drawable.paytm,
                    )



                    Gap(20)



                }
            }

            // Social Media Links Section
            Card(
                modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Social Media Links", style = MaterialTheme.typography.titleMedium
                    )

                    SocialMediaTextField(
                        value = telegramLink,
                        onValueChange = { telegramLink = it },
                        label = "Telegram Link",
                        icon = R.drawable.telegram
                    )

                    SocialMediaTextField(
                        value = indianQr,
                        onValueChange = { indianQr = it },
                        label = "Paytm/GooglePay/PhonePe QR",
                        icon = lucide.image
                    )


                }
            }

            Spacer(modifier = Modifier.height(16.dp))

// Save Button
            val trimmedRocket = rocketNumber.trim()
            val trimmedBkash = bkashNumber.trim()
            val trimmedNagad = nagadNumber.trim()
            val trimmedTelegram = telegramLink.trim()
            val trimmedIndianQr = indianQr.trim()
            val trimmedGooglePay = googlePayNumber.trim()
            val trimmedPhonePe = phonePeNumber.trim()
            val trimmedPaytm = paytmNumber.trim()

            val hasNumberChanged =
                (appInfo.rocketNumber != trimmedRocket || appInfo.bkashNumber != trimmedBkash || appInfo.nagadNumber != trimmedNagad) && trimmedRocket.isNotEmpty() && trimmedBkash.isNotEmpty() && trimmedNagad.isNotEmpty()

            val hasIndianPayChanged = appInfo.phonePeNumber != trimmedPhonePe && trimmedPhonePe.isNotEmpty() || appInfo.googlePayNumber != trimmedGooglePay && trimmedGooglePay.isNotEmpty() || appInfo.paytmNumber != trimmedPaytm && trimmedPaytm.isNotEmpty()

            val hasSocialChanged =
               appInfo.telegramLink != trimmedTelegram && trimmedTelegram.isNotEmpty() || appInfo.indianQr != trimmedIndianQr && trimmedIndianQr.isNotEmpty()


            val isButtonEnabled = hasNumberChanged || hasSocialChanged || hasIndianPayChanged

            Button(
                enabled = isButtonEnabled, onClick = {
                    isLoading = true
                    val appInfo = appInfo.copy(
                        bkashNumber = trimmedBkash,
                        nagadNumber = trimmedNagad,
                        rocketNumber = trimmedRocket,
                        telegramLink = trimmedTelegram,
                        indianQr = trimmedIndianQr,
                        googlePayNumber = trimmedGooglePay,
                        phonePeNumber = trimmedPhonePe,
                        paytmNumber = trimmedPaytm
                    )
                    viewmodel.updateAppInfo(appInfo,
                        onSuccess = {
                            isLoading = false
                            ("Updated Successfully").toast(context)
                        },
                        onFailure = {
                            isLoading=false
                            it?.toast(context)
                        }
                        )

                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "SAVE")
            }



            Spacer(modifier = Modifier.height(16.dp))
        }

            LoadingDialog(loading = isLoading,"Saving...")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: Int,

) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
        ),
        leadingIcon = {
            Icon(
                icon.painter(),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialMediaTextField(
    value: String, onValueChange: (String) -> Unit, label: String, icon: Int
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            imeAction = if (label.lowercase()
                    .startsWith("paytm")
            ) ImeAction.Done else ImeAction.Next, keyboardType = KeyboardType.Uri
        ),
        leadingIcon = {
            Icon(
                icon.painter(),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

