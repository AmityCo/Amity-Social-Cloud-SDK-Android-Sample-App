package com.amity.sample.ascsdk.stream

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.extension.adapter.AmityPagedListAdapter
import com.bumptech.glide.Glide
import com.amity.socialcloud.sdk.video.stream.AmityStream
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.StreamVideoPlayerIntent
import com.google.common.base.Objects
import kotlinx.android.synthetic.main.item_stream.view.*

class StreamListAdapter : AmityPagedListAdapter<AmityStream, StreamListAdapter.StreamViewHolder>(STREAM_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_stream, parent, false)
        return StreamViewHolder(view)
    }

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) {
        val stream = getItem(position)
        if (stream == null) {
            holder.stream = null
            holder.itemView.stream_textview.text = "loading..."
        } else {
            val text = StringBuilder()
                    .append("id: ")
                    .append(stream.getStreamId())
                    .append("\ntitle: ")
                    .append(stream.getTitle())
                    .append("\ndescription : ")
                    .append(stream.getDescription())
                    .append("\nuser : ")
                    .append(stream.getUser()?.getDisplayName() ?: "none")
                    .append("\nrecordings size : ")
                    .append(stream.getRecordings().size)
                    .append("\n-------------------------------------------------")
                    .toString()
            holder.stream = stream
            holder.itemView.stream_textview.text = text

            stream.getThumbnailImage()?.getUrl()?.let { imageUrl ->
                Glide.with(holder.itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .into(holder.itemView.stream_imageview)
            } ?: kotlin.run {
                holder.itemView.stream_imageview.setImageDrawable(null)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    class StreamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var stream: AmityStream? = null

        init {
            itemView.setOnClickListener { view ->
                stream?.let {
                    val context = view.context
                    val intent = StreamVideoPlayerIntent(context, it.getStreamId())
                    context.startActivity(intent)
                }
            }
        }
    }
}


val STREAM_DIFF_CALLBACK: DiffUtil.ItemCallback<AmityStream> = object : DiffUtil.ItemCallback<AmityStream>() {
    override fun areItemsTheSame(oldAmityStream: AmityStream, newAmityStream: AmityStream): Boolean {
        return Objects.equal(oldAmityStream.getStreamId(), newAmityStream.getStreamId())
    }

    override fun areContentsTheSame(oldAmityStream: AmityStream, newAmityStream: AmityStream): Boolean {
        return (Objects.equal(oldAmityStream.getStreamId(), newAmityStream.getStreamId())
                && Objects.equal(oldAmityStream.getTitle(), newAmityStream.getTitle())
                && Objects.equal(oldAmityStream.getDescription(), newAmityStream.getDescription()))
    }
}
