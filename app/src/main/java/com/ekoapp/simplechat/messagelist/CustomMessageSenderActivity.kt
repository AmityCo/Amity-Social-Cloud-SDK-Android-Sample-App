package com.ekoapp.simplechat.messagelist

import android.app.Activity
import android.os.Bundle
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.simplechat.KeyValueInputActivity
import com.ekoapp.simplechat.intent.OpenCustomMessageSenderIntent
import com.google.gson.JsonObject
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_key_value_input.*

class CustomMessageSenderActivity : KeyValueInputActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Send custom"
    }

    override fun onButtonClick() {
        send_button.isEnabled = false
        sendCustomMessage()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun sendCustomMessage() {
        val request = createRequest()
        request.subscribe()
    }

    private fun createRequest(): Completable {
        val messageRepository = EkoClient.newMessageRepository()
        val channelId = OpenCustomMessageSenderIntent.getChannelId(intent) ?: ""
        val parentId = OpenCustomMessageSenderIntent.getParentId(intent)
        val data = JsonObject();
        data.addProperty(key_edittext.text.toString().trim(), value_edittext.text.toString().trim());

        if (parentId != null) {
            return messageRepository
                    .createMessage(channelId)
                    .custom(data)
                    .parentId(parentId)
                    .build()
                    .send()

        } else {
            return messageRepository
                    .createMessage(channelId)
                    .custom(data)
                    .build()
                    .send()
        }
    }

}