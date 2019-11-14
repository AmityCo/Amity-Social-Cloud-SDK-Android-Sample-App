package com.ekoapp.simplechat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.ekoapp.simplechat.intent.OpenCustomMessageEditorActivityIntent
import kotlinx.android.synthetic.main.activity_custom_message_sender.*

class CustomMessageEditorActivity : CustomMessageSenderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Edit custom message"

        send_button.setOnClickListener {
            send_button.isEnabled = false

            val intent = Intent()
            intent.putExtra(OpenCustomMessageEditorActivityIntent.EXTRA_MAP_KEY, key_edittext.text.toString().trim())
            intent.putExtra(OpenCustomMessageEditorActivityIntent.EXTRA_MAP_VALUE, value_edittext.text.toString().trim())
            setResult(Activity.RESULT_OK, intent)

            finish()
        }
    }


}