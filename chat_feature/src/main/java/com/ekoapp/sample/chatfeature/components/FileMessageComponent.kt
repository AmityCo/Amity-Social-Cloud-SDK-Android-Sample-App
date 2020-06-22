package com.ekoapp.sample.chatfeature.components

import android.Manifest
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.messaging.data.FileData
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.data.ReactionData
import com.ekoapp.sample.chatfeature.dialogs.MessageBottomSheetFragment
import com.ekoapp.sample.chatfeature.messages.view.list.ReactionsAdapter
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.file.FileManager
import com.ekoapp.sample.core.rx.into
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.component_file_message.view.*

class FileMessageComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_file_message, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setMessage(item: EkoMessage, items: ArrayList<ReactionData>, reply: (EkoMessage) -> Unit, delete: (Boolean) -> Unit) {
        item.getData(FileData::class.java).apply {
            text_message_url.text = url
            setFileName()
            setCaption()
            renderOpenFile(item)
            val reactions = item.reactions.flatMap { result -> items.filter { result.key == it.name } }
            reactions.renderReactions()
            popupReactionAndReply(items, reply, item)
            renderMessageBottomSheet(delete)
        }
    }

    private fun renderOpenFile(item: EkoMessage) {
        view_message.setOnClickListener {
            Dexter.withContext(context)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                            FileManager.openFile(context, item)
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) {

                        }

                        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                            token.continuePermissionRequest()
                        }
                    }).check()
        }
    }

    private fun popupReactionAndReply(items: ArrayList<ReactionData>, reply: (EkoMessage) -> Unit, item: EkoMessage) {
        view_message.setOnLongClickListener {
            reaction_and_reply.visibility = View.VISIBLE
            return@setOnLongClickListener true
        }
        reaction_and_reply.setupView(items,
                selectedReaction = {
                    reaction_and_reply.visibility = View.GONE
                    item.react().addReaction(it).subscribe() into CompositeDisposable()
                },
                actionReply = {
                    reaction_and_reply.visibility = View.GONE
                    reply.invoke(item)
                })
    }

    private fun FileData.setFileName() {
        if (fileName.isNullOrEmpty()) {
            text_message_file_name.visibility = View.INVISIBLE
        } else {
            text_message_file_name.visibility = View.VISIBLE
            text_message_file_name.text = fileName
        }
    }

    private fun FileData.setCaption() {
        if (caption.isNullOrEmpty()) {
            text_message_caption.visibility = View.GONE
        } else {
            text_message_caption.visibility = View.VISIBLE
            text_message_caption.text = caption
        }
    }

    private fun List<ReactionData>.renderReactions() {
        if (isNotEmpty()) {
            val adapter = ReactionsAdapter(context, this)
            RecyclerBuilder(context, recycler_reactions, size)
                    .builder()
                    .build(adapter)
        } else {
            recycler_reactions.visibility = View.GONE
        }
    }

    private fun renderMessageBottomSheet(delete: (Boolean) -> Unit) {
        image_more.setOnClickListener {
            val messageBottomSheet = MessageBottomSheetFragment()
            messageBottomSheet.show((context as AppCompatActivity).supportFragmentManager, messageBottomSheet.tag)

            messageBottomSheet.renderDelete {
                delete.invoke(it)
                messageBottomSheet.dialog?.cancel()
            }
        }
    }

    fun Boolean.showOrHideAvatar() {
        if (this) {
            avatar.visibility = View.INVISIBLE
        } else {
            avatar.visibility = View.VISIBLE
        }
    }
}