package com.jihan.teeradmin.domain

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.cloudinary.utils.ObjectUtils
import com.jihan.teeradmin.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private fun getRealPathFromUri(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    return if (cursor != null && cursor.moveToFirst()) {
        val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val path = if (index != -1) cursor.getString(index) else null
        cursor.close()
        path
    } else uri.path
}



suspend fun uploadImage(context: Context, imageUri: Uri): String? = withContext(Dispatchers.IO) {
    val app = context.applicationContext as MyApplication
    val cloudinary = app.cloudinary

    val filePath = getRealPathFromUri(context, imageUri)
    if (filePath != null) {
        try {
            val result = cloudinary.uploader().upload(filePath, ObjectUtils.emptyMap())
            result["url"] as? String
        } catch (e: Exception) {
            Log.e("CloudinaryUpload", "Upload failed", e)
            null
        }
    } else {
        Log.e("CloudinaryUpload", "Invalid file path")
        null
    }
}

