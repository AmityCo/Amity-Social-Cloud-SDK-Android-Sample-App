package com.ekoapp.sample.chatfeature.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.core.data.Metadata
import com.ekoapp.sample.core.utils.isEmpty
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_custom_message.*


class CustomMessageBottomSheetFragment : BottomSheetDialogFragment() {

    private var fragmentView: View? = null

    lateinit var callbackSend: (Metadata) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.bottom_sheet_custom_message, container, false)
        return fragmentView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { setupBottomSheet(it) }
        return dialog
    }

    private fun setupBottomSheet(dialogInterface: DialogInterface) {
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet = bottomSheetDialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet)
                ?: return
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        listOf<AppCompatEditText>(edit_text_key, edit_text_value).forEach {
            it.clearComposingText()
            it.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(text: Editable?) {
                    it.removeTextChangedListener(this)
                    context?.setSendButton()
                    it.addTextChangedListener(this)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            })
        }
    }

    private fun Context.setSendButton() {
        if (!edit_text_key.isEmpty() && !edit_text_value.isEmpty()) {
            text_send.setTextColor(ContextCompat.getColor(this, R.color.colorTextEnable))
            text_send.setOnClickListener {
                callbackSend.invoke(Metadata(key = edit_text_key.text.toString(), value = edit_text_value.text.toString()))
            }
        } else {
            text_send.setTextColor(ContextCompat.getColor(this, R.color.colorTextDisable))
        }
    }

    fun sendCustomMessage(callbackSend: (Metadata) -> Unit) {
        this.callbackSend = callbackSend
    }
}