package com.ekoapp.sample.chatfeature.settings.list.renders

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


fun addTags(action: (Set<String>) -> Unit, text: String) {
    val tags = ArrayList<String>()
    tags.add(text)
    action.invoke(tags.toSet())
}

fun EditText.renderEventTags(action: (Set<String>) -> Unit) {
    clearComposingText()
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val text = s?.trim().toString()
            addTags(action, text)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    })
}