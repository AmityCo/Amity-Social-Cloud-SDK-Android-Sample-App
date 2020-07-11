package com.ekoapp.sample.chatfeature.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.core.seals.ReportMessageSealType
import com.ekoapp.sample.core.seals.ReportSenderSealType
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_message.*

class MessageBottomSheetFragment(val item: EkoMessage) : BottomSheetDialogFragment() {

    private var fragmentView: View? = null

    lateinit var callbackDelete: (Boolean) -> Unit
    lateinit var callbackReportMessage: (ReportMessageSealType) -> Unit
    lateinit var callbackReportSender: (ReportSenderSealType) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.bottom_sheet_message, container, false)
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
        setupEvent()
    }

    private fun setupView() {
        renderReportMessageView()
        renderReportSenderView()
    }

    private fun setupEvent() {
        text_delete.setOnClickListener {
            //TODO Show Confirm Dialog
            callbackDelete(true)
        }
        text_report_message.setOnClickListener {
            //TODO Show Confirm Dialog
            if (!item.isFlaggedByMe) {
                callbackReportMessage(ReportMessageSealType.FLAG(item))
            } else {
                callbackReportMessage(ReportMessageSealType.UNFLAG(item))
            }
        }
        text_report_sender.setOnClickListener {
            //TODO Show Confirm Dialog
            if (!item.user.isFlaggedByMe) {
                callbackReportSender(ReportSenderSealType.FLAG(item.user))
            } else {
                callbackReportSender(ReportSenderSealType.UNFLAG(item.user))
            }
        }
    }

    private fun renderReportMessageView() {
        if (!item.isFlaggedByMe) {
            text_report_message.text = getString(R.string.temporarily_report_message)
        } else {
            text_report_message.text = getString(R.string.temporarily_cancel_report_message)
        }
    }

    private fun renderReportSenderView() {
        if (!item.user.isFlaggedByMe) {
            text_report_sender.text = getString(R.string.temporarily_report_sender)
        } else {
            text_report_sender.text = getString(R.string.temporarily_cancel_report_sender)
        }
    }

    fun renderDelete(callbackDelete: (Boolean) -> Unit) {
        this.callbackDelete = callbackDelete
    }

    fun renderReportMessage(callbackReportMessage: (ReportMessageSealType) -> Unit) {
        this.callbackReportMessage = callbackReportMessage
    }

    fun renderReportSender(callbackReportSender: (ReportSenderSealType) -> Unit) {
        this.callbackReportSender = callbackReportSender
    }
}