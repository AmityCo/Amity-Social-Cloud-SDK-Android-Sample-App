package com.ekoapp.sample.socialfeature.userfeed.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekoapp.sample.socialfeature.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_user_feeds_more_horiz.*

class MoreHorizBottomSheetFragment : BottomSheetDialogFragment() {

    private var fragmentView: View? = null

    lateinit var callbackEdit: (Boolean) -> Unit
    lateinit var callbackDelete: (Boolean) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.bottom_sheet_user_feeds_more_horiz, container, false)
        return fragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        text_edit.setOnClickListener {
            //TODO Show Confirm Dialog
//            callbackEdit(true)
        }
        text_delete.setOnClickListener {
            //TODO Show Confirm Dialog
            callbackDelete(true)
        }
    }

    fun renderEdit(callbackEdit: (Boolean) -> Unit) {
        this.callbackEdit = callbackEdit
    }

    fun renderDelete(callbackDelete: (Boolean) -> Unit) {
        this.callbackDelete = callbackDelete
    }
}