package com.ekoapp.simplechat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.simplechat.intent.OpenCustomMessageEditorActivityIntent
import com.ekoapp.simplechat.intent.OpenTextMessageEditorActivityIntent
import kotlinx.android.synthetic.main.activity_custom_message_sender.*
import kotlinx.android.synthetic.main.activity_custom_message_sender.send_button
import kotlinx.android.synthetic.main.activity_text_message_editor.*

class TextMessageEditorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Edit text message"
        setContentView(R.layout.activity_text_message_editor)

        val currentText = OpenTextMessageEditorActivityIntent.getText(intent)

        edittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // do nothing
            }

            override fun afterTextChanged(s: Editable) {
                send_button.isEnabled = !s.isNullOrBlank() && s.toString() != currentText
            }
        })

        send_button.setOnClickListener {
            send_button.isEnabled = false

            val intent = Intent()
            intent.putExtra(OpenTextMessageEditorActivityIntent.EXTRA_TEXT, edittext.text.toString().trim())
            setResult(Activity.RESULT_OK, intent)

            finish()
        }
    }
}