package com.ekoapp.sample.chatfeature.messagelist

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.internal.util.RealPathUtil
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.intent.IntentRequestCode
import com.ekoapp.sample.chatfeature.intent.OpenImageMessageSenderIntent
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

    private var currentPhotoPath: String? = null
    private var currentPhotoUri: Uri? = null

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
                .subscribe({ granted ->
                    if (granted) {
                        dispatchTakePictureIntent()
                    }
                })

        findViewById<View>(R.id.gallery_button).clicks()
                .compose(rxPermissions.ensure<Unit>(Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe({ granted ->
                    if (granted) {
                        dispatchSearchFileIntent()
                    }
                })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IntentRequestCode.REQUEST_TAKE_PHOTO) {
                setImage()
            }
            if (requestCode == IntentRequestCode.REQUEST_SELECT_PHOTO) {
                data?.data?.also { uri ->
                    currentPhotoPath = RealPathUtil.getRealPath(this, uri)
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

        currentPhotoPath = image.absolutePath
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

        BitmapFactory.decodeFile(currentPhotoPath)?.also { bitmap ->

            Glide.with(this)
                    .load(bitmap)
                    .into(imageview)

            val f = File(currentPhotoPath)
            val contentUri = Uri.fromFile(f)
            currentPhotoUri = contentUri

            send_button.isEnabled = true
            textview.visibility = View.GONE
        }

    }

    private fun sendImageMessage() {
        val request = createRequest()
        request.subscribe()

    }

    private fun createRequest(): Completable {
        val messageRepository = EkoClient.newMessageRepository()
        val channelId = OpenImageMessageSenderIntent.getChannelId(intent) ?: ""
        val parentId = OpenImageMessageSenderIntent.getParentId(intent)

        if (parentId != null) {
            return messageRepository.createMessage(channelId)
                    .image(currentPhotoUri)
                    .parentId(parentId)
                    .build()
                    .send()
        } else {
            return messageRepository.createMessage(channelId)
                    .image(currentPhotoUri)
                    .build()
                    .send()
        }

    }

}