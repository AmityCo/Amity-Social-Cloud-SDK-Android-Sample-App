package com.ekoapp.sdk.common.extensions

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.input
import com.ekoapp.sdk.utils.dialog.EkoInputsDialog

@JvmName(name = "KeyboardUtils")
fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.showDialog(@StringRes title: Int, hint: CharSequence, prefill: CharSequence, allowEmptyInput: Boolean, callback: InputCallback) {
    MaterialDialog(this).show {
        title(title)
        input(inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS, hint = hint.toString(), prefill = prefill, allowEmpty = allowEmptyInput, callback = callback)
    }
}

fun Context.showDialog(@StringRes title: Int, hint: CharSequence, prefill: CharSequence, allowEmptyInput: Boolean, callback: InputCallback) {
    MaterialDialog(this).show {
        title(title)
        input(inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS, hint = hint.toString(), prefill = prefill, allowEmpty = allowEmptyInput, callback = callback)
    }
}

fun AppCompatActivity.showEkoInputsDialog(firstInputHint: String, secondInputHint: String,
                                          textBtnLeft: String, textBtnRight: String) {
    EkoInputsDialog.Builder()
            .firstInputHint(firstInputHint)
            .secondInputHint(secondInputHint)
            .textBtnLeftDialog(textBtnLeft)
            .textBtnRightDialog(textBtnRight)
            .build()
            .show(supportFragmentManager, "")
}
