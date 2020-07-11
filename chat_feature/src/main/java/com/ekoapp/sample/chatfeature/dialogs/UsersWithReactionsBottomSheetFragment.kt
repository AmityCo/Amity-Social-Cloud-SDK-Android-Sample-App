package com.ekoapp.sample.chatfeature.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channels.list.UsersWithReactionsAdapter
import com.ekoapp.sample.chatfeature.messages.view.MessagesViewModel
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_users_reactions.*

class UsersWithReactionsBottomSheetFragment(private val item: EkoMessage,
                                            private val lifecycleOwner: LifecycleOwner,
                                            private val viewModel: MessagesViewModel) : BottomSheetDialogFragment() {

    private lateinit var adapter: UsersWithReactionsAdapter
    private var fragmentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.bottom_sheet_users_reactions, container, false)
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
        context?.renderList()
    }

    private fun Context.renderList() {
        adapter = UsersWithReactionsAdapter(this, viewModel.getReactions())
        RecyclerBuilder(context = this, recyclerView = recycler_users_reactions)
                .builder()
                .build(adapter)
        viewModel.bindGetMessageReactionCollection(item.messageId).observeNotNull(lifecycleOwner, adapter::submitList)
    }
}