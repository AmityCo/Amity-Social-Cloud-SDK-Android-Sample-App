package com.ekoapp.sdk.messagelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import butterknife.Setter
import butterknife.ViewCollections
import com.bumptech.glide.Glide
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.adapter.EkoMessageAdapter
import com.ekoapp.ekosdk.file.EkoImage
import com.ekoapp.ekosdk.message.EkoMessage
import com.ekoapp.sdk.R
import com.google.common.base.Joiner
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
    private val compositeDisposable = CompositeDisposable()


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
                    holder.itemView.progress_horizontal.visibility = View.GONE
                    holder.itemView.data_textview.text =
                            String.format("data type: %s,\ndata: %s", type, "")

                    val imageData = data.getImage()
                    if (!imageData?.getUrl().isNullOrEmpty()) {
                        Glide.with(holder.itemView.data_imageview.context)
                                .load(imageData?.getUrl(EkoImage.Size.SMALL))
                                .into(holder.itemView.data_imageview)
                    } else {
                        Glide.with(holder.itemView.data_imageview.context)
                                .load(imageData?.getFilePath())
                                .into(holder.itemView.data_imageview)
                    }

                    if (m.getState() == EkoMessage.State.UPLOADING) {
                        val disposable = EkoClient.newFileRepository()
                                .getUploadInfo(data.getMessageId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext {
                                    val progress = it.getProgressPercentage()
                                    holder.itemView.progress_horizontal.progress = progress
                                    holder.itemView.progress_horizontal.visibility = View.VISIBLE
                                }
                                .subscribe()
                        compositeDisposable.add(disposable)
                    }
                }
                is EkoMessage.Data.FILE -> {
                    holder.itemView.data_imageview.visibility = View.GONE
                    holder.itemView.progress_horizontal.visibility = View.GONE
                    holder.itemView.data_textview.text =
                            String.format("data type: %s, \ndata: %s , caption: %s ",
                                    type,
                                    data.getFile()?.getUrl() ?: data.getFile()?.getFilePath(),
                                    data.getCaption())

                    if (m.getState() == EkoMessage.State.UPLOADING) {
                        val disposable = EkoClient.newFileRepository()
                                .getUploadInfo(data.getMessageId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext {
                                    val progress = it.getProgressPercentage()
                                    holder.itemView.progress_horizontal.progress = progress
                                    holder.itemView.progress_horizontal.visibility = View.VISIBLE
                                }
                                .subscribe()
                        compositeDisposable.add(disposable)
                    }
                }
                is EkoMessage.Data.AUDIO -> {
                    holder.itemView.data_imageview.visibility = View.GONE
                    holder.itemView.progress_horizontal.visibility = View.GONE
                    holder.itemView.data_textview.text =
                            String.format("data type: %s, \ndata: %s",
                                    type,
                                    data.getAudio()?.getUrl() ?: data.getAudio()?.getFilePath())

                    if (m.getState() == EkoMessage.State.UPLOADING) {
                        val disposable = EkoClient.newFileRepository()
                                .getUploadInfo(data.getMessageId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext {
                                    val progress = it.getProgressPercentage()
                                    holder.itemView.progress_horizontal.progress = progress
                                    holder.itemView.progress_horizontal.visibility = View.VISIBLE
                                }
                                .subscribe()
                        compositeDisposable.add(disposable)
                    }
                }
                else -> {

                }
            }
            renderReaction(holder, m)
            holder.itemView.sync_state_textview.text = m.getState().stateName
            holder.itemView.time_textview.text = created.toString(DateTimeFormat.longDateTime())
        }
        holder.itemView.setOnLongClickListener { v ->
            onLongClickSubject.onNext(m!!)
            true
        }
        holder.itemView.setOnClickListener { onClickSubject.onNext(m!!) }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        compositeDisposable.clear()
        super.onDetachedFromRecyclerView(recyclerView)
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
