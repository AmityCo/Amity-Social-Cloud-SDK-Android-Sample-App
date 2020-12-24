package com.ekoapp.sdk.common.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.sdk.R
import kotlinx.android.synthetic.main.activity_key_value_input.*

abstract class KeyValueInputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_key_value_input)
        send_button.isEnabled = false
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
            onButtonClick()
        }

    }

    abstract fun onButtonClick()

}