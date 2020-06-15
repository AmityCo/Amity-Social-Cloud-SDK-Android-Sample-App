package com.ekoapp.sample.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.ekoapp.sample.core.intent.IntentRequestCode
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun AppCompatActivity.dispatchTakePictureIntent(path: (String) -> Unit) {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (takePictureIntent.resolveActivity(packageManager) != null) {
        var photoFile: File? = null
        try {
            photoFile = createImageFile(path)
        } catch (ex: IOException) {
            Timber.d("${getCurrentClassAndMethodNames()} ${ex.message}")
        }

        if (photoFile != null) {
            val photoURI = FileProvider.getUriForFile(this, packageName, photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, IntentRequestCode.REQUEST_TAKE_PHOTO)
        }
    }
}

@Throws(IOException::class)
private fun Context.createImageFile(path: (String) -> Unit): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
    )

    path.invoke(image.absolutePath)
    return image
}

fun AppCompatActivity.dispatchSearchImageFileIntent() {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "image/*"
    }
    startActivityForResult(intent, IntentRequestCode.REQUEST_SELECT_PHOTO)
}

fun String.getRealUri(): Uri {
    val f = File(this)
    return Uri.fromFile(f)
}