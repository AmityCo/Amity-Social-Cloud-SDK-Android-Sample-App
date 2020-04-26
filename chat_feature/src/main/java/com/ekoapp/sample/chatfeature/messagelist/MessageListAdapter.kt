package com.ekoapp.sample.chatfeature.messagelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.EkoObjects
import com.ekoapp.ekosdk.adapter.EkoMessageAdapter
import com.ekoapp.ekosdk.messaging.data.DataType
import com.ekoapp.ekosdk.messaging.data.FileData
import com.ekoapp.ekosdk.messaging.data.ImageData
import com.ekoapp.ekosdk.messaging.data.TextData
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.messagelist.MessageListAdapter.MessageViewHolder
import com.google.common.base.Joiner
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_message.view.*
import org.joda.time.format.DateTimeFormat

class MessageListAdapter : EkoMessageAdapter<MessageViewHolder>() {
    private val onLongClickSubject = PublishSubject.create<EkoMessage>()
    private val onClickSubject = PublishSubject.create<EkoMessage>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val m = getItem(position)
        if (EkoObjects.isProxy(m)) {
            renderLoadingItem(holder, position)
        } else if (m!!.isDeleted) {
            renderDeletedMessage(holder)
        } else {
            setViewsVisibility(holder.optionalViews, View.VISIBLE)
            val type = m.type
            val sender = m.user
            val created = m.createdAt
            holder.itemView.message_textview.text = String.format("mid: %s %s:%s\nsegment: %s",
                    m.messageId,
                    if (m.isFlaggedByMe) "\uD83C\uDFC1" else "\uD83C\uDFF3️",
                    m.flagCount,
                    m.channelSegment)
            holder.itemView.sender_textview.text = String.format("uid: %s %s: %s\ndisplay name: %s",
                    sender?.userId ?: "",
                    if (sender != null && sender.isFlaggedByMe) "\uD83C\uDFC1" else "\uD83C\uDFF3️",
                    sender?.flagCount ?: 0,
                    if (sender != null) sender.displayName else "")
            holder.itemView.comment_count_textview.text = String.format("comment count: %s", m.childrenNumber)
            holder.itemView.tags_textview.text = String.format("tags️: %s", Joiner.on(", ").join(m.tags))
            if (DataType.TEXT == DataType.from(m.type)) {
                holder.itemView.data_textview.text = String.format("text: %s", m.getData(TextData::class.java).text)
                Glide.with(holder.itemView.data_imageview!!.context).clear(holder.itemView.data_imageview)
            } else if (DataType.IMAGE == DataType.from(m.type)) {
                val url = m.getData(ImageData::class.java).url
                holder.itemView.data_textview.text = String.format("data type: %s,\nurl: %s,\ndata: ", type, url) + m.data.toString()
                Glide.with(holder.itemView.data_imageview.context).load(url).into(holder.itemView.data_imageview)
            } else if (DataType.FILE == DataType.from(m.type)) {
                val url = m.getData(FileData::class.java).url
                holder.itemView.data_textview.text = String.format("data type: %s,\nurl: %s,\ndata: ", type, url) + m.data.toString()
                Glide.with(holder.itemView.data_imageview.context).clear(holder.itemView.data_imageview)
            } else {
                holder.itemView.data_textview.text = String.format("data type: %s,\ndata: ", type) + m.data.toString()
                Glide.with(holder.itemView.data_imageview.context).clear(holder.itemView.data_imageview)
            }
            renderReaction(holder, m)
            holder.itemView.sync_state_textview.text = m.syncState.name
            holder.itemView.time_textview.text = created.toString(DateTimeFormat.longDateTime())
        }
        holder.itemView.setOnLongClickListener { v: View? ->
            onLongClickSubject.onNext(m!!)
            true
        }
        holder.itemView.setOnClickListener { v: View? -> onClickSubject.onNext(m!!) }
    }

    private fun renderLoadingItem(holder: MessageViewHolder, position: Int) {
        setViewsVisibility(holder.optionalViews, View.GONE)
        holder.itemView.message_textview.text = String.format("loading adapter position: %s", position)
        Glide.with(holder.itemView.data_imageview.context).clear(holder.itemView.data_imageview)
    }

    private fun renderDeletedMessage(holder: MessageViewHolder) {
        setViewsVisibility(holder.optionalViews, View.GONE)
        holder.itemView.message_textview.text = String.format("Deleted")
        holder.itemView.data_textview.text = ""
        Glide.with(holder.itemView.data_imageview.context).clear(holder.itemView.data_imageview)
    }

    private fun renderReaction(holder: MessageViewHolder, m: EkoMessage) {
        var reactions = ""
        for (key in m.reactions.keys) {
            val reactionCount = m.reactions.getCount(key)
            reactions += String.format("\n%s : %s", key, reactionCount)
        }
        val myReactions = m.myReactions
        val myReactionsString = Joiner.on(" ").join(myReactions)
        holder.itemView.reaction_textview.text = String.format("reaction count: %s %s \nmy reactions: %s",
                m.reactionCount,
                reactions,
                myReactionsString)
    }

    val onLongClickFlowable: Flowable<EkoMessage>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<EkoMessage>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var optionalViews: List<View> = listOf(itemView.sender_textview,
                itemView.data_textview,
                itemView.sync_state_textview,
                itemView.time_textview)
    }

    private fun setViewsVisibility(views: List<View>, visibility: Int) {
        views.forEach {
            it.visibility = visibility
        }
    }

}
