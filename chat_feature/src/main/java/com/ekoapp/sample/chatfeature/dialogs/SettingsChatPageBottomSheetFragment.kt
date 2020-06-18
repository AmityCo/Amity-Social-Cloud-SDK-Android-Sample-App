package com.ekoapp.sample.chatfeature.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekoapp.sample.chatfeature.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_settings_chat_page.*

class SettingsChatPageBottomSheetFragment : BottomSheetDialogFragment() {

    private var fragmentView: View? = null

    lateinit var callbackGeneral: () -> Unit
    lateinit var callbackChannel: () -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.bottom_sheet_settings_chat_page, container, false)
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
        initView()
    }

    private fun initView() {
        text_general.setOnClickListener {
            callbackGeneral()
        }
        text_channel.setOnClickListener {
            callbackChannel()
        }
    }

    fun renderGeneral(callbackGeneral: () -> Unit) {
        this.callbackGeneral = callbackGeneral
    }

    fun renderChannel(callbackChannel: () -> Unit) {
        this.callbackChannel = callbackChannel
    }
}