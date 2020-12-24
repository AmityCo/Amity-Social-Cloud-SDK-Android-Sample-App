package com.ekoapp.sdk.messagelist

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.internal.util.RealPathUtil
import com.ekoapp.sdk.R
import com.ekoapp.sdk.intent.IntentRequestCode.REQUEST_SELECT_FILE
import com.ekoapp.sdk.intent.OpenFileMessageSenderIntent
import com.jakewharton.rxbinding3.view.clicks
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_file_message_sender.*

class FileMessageSenderActivity : AppCompatActivity() {

    private val defaultText = "No file selected"

    private var currentFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Send file"
        setContentView(R.layout.activity_file_message_sender)

        textview.text = defaultText

        send_button.isEnabled = false
        send_button.setOnClickListener {
            send_button.isEnabled = false
            sendFileMessage()
            setResult(Activity.RESULT_OK)
            finish()
        }

        val rxPermissions = RxPermissions(this)
        findViewById<View>(R.id.file_button).clicks()
                .compose(rxPermissions.ensure<Unit>(Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe({ granted ->
                    if (granted) {
                        dispatchSearchFileIntent()
                    }
                })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_FILE && resultCode == Activity.RESULT_OK) {
            setFile(data)
        }

    }

    private fun dispatchSearchFileIntent() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, REQUEST_SELECT_FILE)

    }

    private fun setFile(data: Intent?) {
        send_button.isEnabled = false
        textview.text = defaultText

        data?.data?.also { uri ->
            currentFileUri = uri

            send_button.isEnabled = true
            val fileName = RealPathUtil.getFileName(this.contentResolver, uri)
            textview.text = "Selected: " + fileName
        }

    }

    private fun sendFileMessage() {
        val request = createRequest()
        request.subscribe()

    }

    private fun createRequest(): Completable {
        val messageRepository = EkoClient.newMessageRepository()
        val channelId = OpenFileMessageSenderIntent.getChannelId(intent) ?: ""
        val parentId = OpenFileMessageSenderIntent.getParentId(intent)

        return messageRepository.createMessage(channelId)
                .parentId(parentId)
                .with()
                .file(currentFileUri!!)
                .build()
                .send()

    }

}