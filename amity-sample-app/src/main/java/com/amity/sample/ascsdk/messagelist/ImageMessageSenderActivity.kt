package com.amity.sample.ascsdk.messagelist

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.amity.socialcloud.sdk.chat.AmityChatClient
import com.bumptech.glide.Glide
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.IntentRequestCode
import com.amity.sample.ascsdk.intent.OpenImageMessageSenderIntent
import com.jakewharton.rxbinding3.view.clicks
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_image_message_sender.*
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageMessageSenderActivity : AppCompatActivity() {

    private var currentPhotoUri: Uri? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Send image"
        setContentView(R.layout.activity_image_message_sender)

        send_button.isEnabled = false
        send_button.setOnClickListener {
            send_button.isEnabled = false
            sendImageMessage()
            setResult(Activity.RESULT_OK)
            finish()
        }

        val rxPermissions = RxPermissions(this)
        findViewById<View>(R.id.camera_button).clicks()
                .compose(rxPermissions.ensure<Unit>(Manifest.permission.CAMERA))
                .subscribe { granted ->
                    if (granted) {
                        dispatchTakePictureIntent()
                    }
                }

        findViewById<View>(R.id.gallery_button).clicks()
                .compose(rxPermissions.ensure<Unit>(Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe { granted ->
                    if (granted) {
                        dispatchSearchFileIntent()
                    }
                }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IntentRequestCode.REQUEST_TAKE_PHOTO) {
                setImage()
            }
            if (requestCode == IntentRequestCode.REQUEST_SELECT_PHOTO) {
                data?.data?.also { uri ->
                    currentPhotoUri = uri
                    setImage()
                }
            }
        }

    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                Timber.e(ex, "Error creating image file")
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this,
                        packageName,
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, IntentRequestCode.REQUEST_TAKE_PHOTO)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        currentPhotoUri = image.toUri()
        return image

    }

    private fun dispatchSearchFileIntent() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, IntentRequestCode.REQUEST_SELECT_PHOTO)

    }


    private fun setImage() {
        send_button.isEnabled = false
        textview.visibility = View.VISIBLE
        Glide.with(this).clear(imageview)
        Glide.with(this)
                .load(currentPhotoUri)
                .into(imageview)

        send_button.isEnabled = true
        textview.visibility = View.GONE
    }

    private fun sendImageMessage() {
        val request = createRequest()
        request.subscribe()
    }

    private fun createRequest(): Completable {
        val messageRepository = AmityChatClient.newMessageRepository()
        val channelId = OpenImageMessageSenderIntent.getChannelId(intent) ?: ""
        val parentId = OpenImageMessageSenderIntent.getParentId(intent)

        return messageRepository.createMessage(channelId)
                .parentId(parentId)
                .with()
                .image(currentPhotoUri!!)
                .build()
                .send()
    }
}