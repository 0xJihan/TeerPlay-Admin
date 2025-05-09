package com.jihan.teeradmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jihan.composeutils.toast
import com.jihan.teeradmin.ui.theme.AppTheme


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
//        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {

            AppTheme {
            NavigationController()
            }


            var time = 0L
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (System.currentTimeMillis() - time < 2000) {
                        finish()
                    } else {
                        time = System.currentTimeMillis()
                        "Press again to exit".toast(this@MainActivity)
                    }
                }
            })

        }

    }


}



