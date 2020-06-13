package com.ekoapp.sample.core.utils

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.sample.core.constants.REQUEST_CODE_CAMERA
import com.ekoapp.sample.core.constants.REQUEST_CODE_GALLERY
import java.io.ByteArrayOutputStream


fun AppCompatActivity.openCamera() {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    startActivityForResult(intent, REQUEST_CODE_CAMERA)
}

fun AppCompatActivity.openGalleryForImage() {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(intent, REQUEST_CODE_GALLERY)
}

fun AppCompatActivity.getImageUri(src: Bitmap, format: CompressFormat?, quality: Int): Uri {
    val os = ByteArrayOutputStream()
    src.compress(format, quality, os)
    val path = MediaStore.Images.Media.insertImage(contentResolver, src, "title", null)
    return Uri.parse(path)
}