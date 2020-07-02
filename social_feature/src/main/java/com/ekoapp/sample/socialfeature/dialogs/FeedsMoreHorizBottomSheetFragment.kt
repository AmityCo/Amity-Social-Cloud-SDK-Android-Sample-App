package com.ekoapp.sample.socialfeature.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.core.seals.ReportSealType
import com.ekoapp.sample.core.seals.ReportSealType.FLAG
import com.ekoapp.sample.core.seals.ReportSealType.UNFLAG
import com.ekoapp.sample.socialfeature.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_user_feeds_more_horiz.*

class FeedsMoreHorizBottomSheetFragment(val item: EkoPost) : BottomSheetDialogFragment() {
    private var fragmentView: View? = null

    lateinit var callbackEdit: (Boolean) -> Unit
    lateinit var callbackDelete: (Boolean) -> Unit
    lateinit var callbackReport: (ReportSealType) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.bottom_sheet_user_feeds_more_horiz, container, false)
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
        renderReportView()
        if (item.postedUserId != EkoClient.getUserId()) {
            renderMoreOtherPost()
        } else {
            renderMoreMyPost()
        }
    }

    private fun renderReportView() {
        if (!item.isFlaggedByMe) {
            text_report.text = getString(R.string.post_report)
        } else {
            text_report.text = getString(R.string.post_cancel_report)
        }
    }

    private fun setupEvent() {
        text_edit.setOnClickListener {
            //TODO Show Confirm Dialog
            callbackEdit(true)
        }

        text_delete.setOnClickListener {
            //TODO Show Confirm Dialog
            callbackDelete(true)
        }

        text_report.setOnClickListener {
            //TODO Show Confirm Dialog
            if (!item.isFlaggedByMe) {
                callbackReport(FLAG(item))
            } else {
                callbackReport(UNFLAG(item))
            }
        }
    }

    fun renderEdit(callbackEdit: (Boolean) -> Unit) {
        this.callbackEdit = callbackEdit
    }

    fun renderDelete(callbackDelete: (Boolean) -> Unit) {
        this.callbackDelete = callbackDelete
    }

    fun renderReport(callbackReport: (ReportSealType) -> Unit) {
        this.callbackReport = callbackReport
    }

    private fun renderMoreMyPost() {
        text_edit.visibility = View.VISIBLE
        text_delete.visibility = View.VISIBLE
        text_report.visibility = View.VISIBLE
    }

    private fun renderMoreOtherPost() {
        text_edit.visibility = View.GONE
        text_delete.visibility = View.GONE
        text_report.visibility = View.VISIBLE
    }
}