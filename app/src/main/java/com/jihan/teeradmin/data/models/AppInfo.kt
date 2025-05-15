package com.jihan.teeradmin.data.models

data class AppInfo(
    val indianQr: String="N/A",
    val bkashNumber: String="N/A",
    val nagadNumber: String="N/A",
    val rocketNumber: String="N/A",
    val whatsAppLink: String="N/A",

    val googlePayNumber: String="N/A",
    val phonePeNumber: String="N/A",
    val paytmNumber: String="N/A",


    val fbLink: String="N/A",
    val fbGroupLink: String="N/A",

    val appVersion: String="",
    val appLink: String="",
    val changeLog: String="",
    val forceAppUpdate: Boolean=true,

    val withdrawVideo: String="",
    val playNowVideo: String="",

)
