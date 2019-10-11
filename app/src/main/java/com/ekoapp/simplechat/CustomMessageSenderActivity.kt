package com.ekoapp.simplechat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.simplechat.intent.OpenCustomMessageSenderActivityIntent
import com.google.gson.JsonObject
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_custom_message_sender.*


class CustomMessageSenderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Send custom"
        setContentView(R.layout.activity_custom_message_sender)

        key_edittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // do nothing
            }

            override fun afterTextChanged(s: Editable) {
                send_button.isEnabled = !s.isNullOrBlank()
            }
        })

        send_button.setOnClickListener {
            send_button.isEnabled = false
            sendCustomMessage()
            finish()
        }

    }

    private fun sendCustomMessage() {
        val request = createRequest()
        request.subscribe()

    }

    private fun createRequest(): Completable {
        val messageRepository = EkoClient.newMessageRepository()
        val channelId = OpenCustomMessageSenderActivityIntent.getChannelId(intent) ?: ""
        val parentId = OpenCustomMessageSenderActivityIntent.getParentId(intent)
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