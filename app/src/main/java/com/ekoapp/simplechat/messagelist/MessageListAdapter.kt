package com.ekoapp.simplechat.messagelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import butterknife.Setter
import butterknife.ViewCollections
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ekoapp.ekosdk.adapter.EkoMessageAdapter
import com.ekoapp.ekosdk.message.EkoMessage
import com.ekoapp.simplechat.R
import com.google.common.base.Joiner
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_message.view.*
import org.joda.time.format.DateTimeFormat

class MessageListAdapter : EkoMessageAdapter<MessageListAdapter.MessageViewHolder>(object : DiffUtil.ItemCallback<EkoMessage>() {

    override fun areItemsTheSame(oldItem: EkoMessage, newItem: EkoMessage): Boolean {
        return oldItem.getMessageId() == newItem.getMessageId()
    }

    override fun areContentsTheSame(oldItem: EkoMessage, newItem: EkoMessage): Boolean {
        return oldItem.getState() == newItem.getState()
                && oldItem.getData() == newItem.getData()
                && oldItem.getFlagCount() == newItem.getFlagCount()
                && oldItem.getReactionCount() == newItem.getReactionCount()
                && oldItem.getUser() == newItem.getUser()
    }
}) {
    private val onLongClickSubject = PublishSubject.create<EkoMessage?>()
    private val onClickSubject = PublishSubject.create<EkoMessage?>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val m = getItem(position)
        val visibility = Setter { view: View, value: Int?, index: Int -> view.visibility = value!! }
        if (m == null) {
            renderLoadingItem(holder, visibility, position)
        } else if (m.isDeleted()) {
            renderDeletedMessage(holder, visibility)
        } else {
            ViewCollections.set(holder.optionalViews, visibility, View.VISIBLE)
            val type = m.getDataType().apiKey
            val sender = m.getUser()
            val created = m.getCreatedAt()
            holder.itemView.message_textview.text = String.format("mid: %s %s:%s\nsegment: %s",
                    m.getMessageId(),
                    if (m.isFlaggedByMe()) "\uD83C\uDFC1" else "\uD83C\uDFF3️",
                    m.getFlagCount(),
                    m.getChannelSegment())
            holder.itemView.sender_textview.text = String.format("uid: %s %s: %s\ndisplay name: %s",
                    sender?.getUserId() ?: "",
                    if (sender != null && sender.isFlaggedByMe()) "\uD83C\uDFC1" else "\uD83C\uDFF3️",
                    sender?.getFlagCount() ?: 0,
                    if (sender != null) sender.getDisplayName() else "")
            holder.itemView.comment_count_textview.text = String.format("comment count: %s", m.getChildrenNumber())
            holder.itemView.tags_textview.text = String.format("tags️: %s", Joiner.on(", ").join(m.getTags()))

            val data = m.getData()
            when (data) {
                is EkoMessage.Data.TEXT -> {
                    holder.itemView.data_imageview.visibility = View.GONE
                    holder.itemView.progress_horizontal.visibility = View.GONE
                    holder.itemView.data_textview.text =
                            String.format("data type: %s,\ndata: %s", type, data.getText())
                }
                is EkoMessage.Data.CUSTOM -> {
                    holder.itemView.data_imageview.visibility = View.GONE
                    holder.itemView.progress_horizontal.visibility = View.GONE
                    holder.itemView.data_textview.text =
                            String.format("data type: %s,\ndata: %s", type, data.raw().toString())
                }
                is EkoMessage.Data.IMAGE -> {
                    holder.itemView.data_imageview.visibility = View.VISIBLE
                    holder.itemView.data_textview.text =
                            String.format("data type: %s,\ndata: %s", type, "")

                    val uri = data.getUri()
                    if (uri != null) {
                        Glide.with(holder.itemView.data_imageview.context)
                                .load(uri)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(data.getWidth(), data.getHeight())
                                .into(holder.itemView.data_imageview)
                    } else {
                        val url = data.getUrl()
                        Glide.with(holder.itemView.data_imageview.context)
                                .load(url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(data.getWidth(), data.getHeight())
                                .into(holder.itemView.data_imageview)
                    }
                    val progress = data.getUploadProgressPercentage()
                    holder.itemView.progress_horizontal.setProgress(progress)
                    when (m.getState()) {
                        EkoMessage.State.CREATED -> {
                            holder.itemView.progress_horizontal.setProgress(1)
                            holder.itemView.progress_horizontal.visibility = View.VISIBLE
                        }
                        EkoMessage.State.UPLOADING -> {
                            holder.itemView.progress_horizontal.visibility = View.VISIBLE
                        }
                        EkoMessage.State.SYNCING -> {
                            holder.itemView.progress_horizontal.setProgress(100)
                            holder.itemView.progress_horizontal.visibility = View.VISIBLE
                        }
                        EkoMessage.State.SYNCED -> {
                            holder.itemView.progress_horizontal.visibility = View.GONE

                        }
                        EkoMessage.State.FAILED -> {
                            holder.itemView.data_imageview.visibility = View.GONE
                            holder.itemView.progress_horizontal.visibility = View.GONE
                        }
                    }
                }
                is EkoMessage.Data.FILE -> {
                    holder.itemView.data_imageview.visibility = View.GONE
                    holder.itemView.data_textview.text =
                            String.format("data type: %s, \ndata: %s", type, data.toString())
                    val progress = data.getUploadProgressPercentage()
                    holder.itemView.progress_horizontal.setProgress(progress)

                    when (m.getState()) {
                        EkoMessage.State.CREATED -> {
                            holder.itemView.progress_horizontal.setProgress(1)
                            holder.itemView.progress_horizontal.visibility = View.VISIBLE
                        }
                        EkoMessage.State.UPLOADING -> {
                            holder.itemView.progress_horizontal.visibility = View.VISIBLE
                        }
                        EkoMessage.State.SYNCING -> {
                            holder.itemView.progress_horizontal.setProgress(100)
                            holder.itemView.progress_horizontal.visibility = View.VISIBLE
                        }
                        EkoMessage.State.SYNCED, EkoMessage.State.FAILED -> {
                            holder.itemView.progress_horizontal.visibility = View.GONE
                        }
                    }
                }
            }
            renderReaction(holder, m)
            holder.itemView.sync_state_textview.text = m.getState().stateName
            holder.itemView.time_textview.text = created.toString(DateTimeFormat.longDateTime())
        }
        holder.itemView.setOnLongClickListener {
            onLongClickSubject.onNext(m!!)
            true
        }
        holder.itemView.setOnClickListener({ v -> onClickSubject.onNext(m!!) })
    }

    private fun renderLoadingItem(holder: MessageViewHolder, visibility: Setter<View, Int?>, position: Int) {
        ViewCollections.set(holder.optionalViews, visibility, View.GONE)
        holder.itemView.message_textview.text = String.format("loading adapter position: %s", position)
        Glide.with(holder.itemView.data_imageview.context).clear(holder.itemView.data_imageview)
    }

    private fun renderDeletedMessage(holder: MessageViewHolder, visibility: Setter<View, Int?>) {
        ViewCollections.set(holder.optionalViews, visibility, View.GONE)
        holder.itemView.message_textview.text = String.format("Deleted")
        holder.itemView.data_textview.text = ""
        holder.itemView.data_imageview.visibility = View.GONE
        holder.itemView.progress_horizontal.visibility = View.GONE
    }

    private fun renderReaction(holder: MessageViewHolder, m: EkoMessage) {
        var reactions = ""
        for (key in m.getReactionMap().keys) {
            val reactionCount = m.getReactionMap().getCount(key)
            reactions += String.format("\n%s : %s", key, reactionCount)
        }
        val myReactions = m.getMyReactions()
        val myReactionsString = Joiner.on(" ").join(myReactions)
        holder.itemView.reaction_textview.text = String.format("reaction count: %s %s \nmy reactions: %s",
                m.getReactionCount(),
                reactions,
                myReactionsString)
    }

    val onLongClickFlowable: Flowable<EkoMessage?>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<EkoMessage?>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var optionalViews: List<View> = listOf(itemView.sender_textview, itemView.data_textview, itemView.sync_state_textview, itemView.time_textview)

    }
}
