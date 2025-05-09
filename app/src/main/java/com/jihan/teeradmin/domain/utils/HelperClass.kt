package com.jihan.teeradmin.domain.utils

import androidx.compose.ui.graphics.Color
import kotlin.random.Random


fun getRandomMaterialStyleColor(): Color {
    val red = Random.nextInt(100, 230)
    val green = Random.nextInt(100, 230)
    val blue = Random.nextInt(100, 230)

    val colorInt = (0xFF shl 24) or (red shl 16) or (green shl 8) or blue
    return Color(colorInt)
}





fun hexToColor(hex: String): Color {
    val cleanHex = hex.removePrefix("#")
    val colorLong = cleanHex.toLong(16)
    return if (cleanHex.length == 6) {
        Color(color = 0xFF000000 or colorLong) // Add full alpha
    } else if (cleanHex.length == 8) {
        Color(color = colorLong)
    } else {
        throw IllegalArgumentException("Invalid hex color: $hex")
    }
}

