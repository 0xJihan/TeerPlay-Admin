package com.jihan.teeradmin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.cloudinary.Cloudinary
import com.google.firebase.FirebaseApp
import com.jihan.teeradmin.data.di.appModule
import com.jihan.teeradmin.domain.utils.Constants
import com.jihan.teeradmin.domain.utils.Constants.APP_ID
import com.michaelflisar.composethemer.ComposeTheme
import com.michaelflisar.composethemer.themes.ComposeThemes
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApplication : Application() {
lateinit var cloudinary: Cloudinary
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule)
        }

        createNotificationChannels()
        ComposeTheme.register(*ComposeThemes.ALL.toTypedArray())


        OneSignal.initWithContext(this, APP_ID)
        OneSignal.Debug.logLevel = LogLevel.VERBOSE
        FirebaseApp.initializeApp(this)

        setupNotificationHandlers()
        subscribeToTopic("admin")





        val config = mapOf(
            "cloud_name" to "teerplay",
            "api_key" to Constants.CLOUDINARY_API_KEY,
            "api_secret" to Constants.CLOUDINARY_API_SECRET,

            )

        cloudinary=Cloudinary(config)
    }





    private fun setupNotificationHandlers() {


        if (OneSignal.Notifications.canRequestPermission){
            CoroutineScope(Dispatchers.Default).launch {
                OneSignal.Notifications.requestPermission(true)
            }
        }


        if (!OneSignal.User.pushSubscription.optedIn) {
            // Ask for permission first, then:
            OneSignal.User.pushSubscription.optIn()
        }


    }

    private fun subscribeToTopic(topic: String) {
        // Add a tag with the topic name
        OneSignal.User.addTag("topic_$topic", "true")
        Log.d("OneSignal", "Subscribed to topic: $topic")
    }


    private fun createNotificationChannels() {
        val carouselChannel = NotificationChannel(
            "admin", // ← This ID must match exactly what you use in API calls
            "Admin Notification", NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for only Admin App"
        }

        // Register channels
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(carouselChannel)

        // Log confirmation
        Log.d("Notifications", "Created  notifications channel")
    }


}