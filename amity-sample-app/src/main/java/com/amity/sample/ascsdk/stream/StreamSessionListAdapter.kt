package com.amity.sample.ascsdk.stream

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.stream.entity.EkoStreamSessionEntity
import com.amity.sample.ascsdk.R
import kotlinx.android.synthetic.main.item_stream_session.view.*

class StreamSessionListAdapter : RecyclerView.Adapter<StreamSessionListAdapter.StreamSessionViewHolder>() {

    private var sessionList = listOf<EkoStreamSessionEntity>()

    fun submitList(sessionList: List<EkoStreamSessionEntity>) {
        this.sessionList = sessionList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamSessionViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_stream_session, parent, false)
        return StreamSessionViewHolder(view)
    }

    fun getItem(position: Int): EkoStreamSessionEntity {
        return sessionList[position]
    }

    override fun onBindViewHolder(holder: StreamSessionViewHolder, position: Int) {
        val context = holder.itemView.stream_title_textview.context
        val streamSession = getItem(position)
        val startTimeText = DateFormat.format("MMMM dd yyyy, h:mm aa", streamSession.startTime?.millis
                ?: 0).toString()
        val endTimeText = DateFormat.format("MMMM dd yyyy, h:mm aa", streamSession.endTime?.millis
                ?: 0).toString()
        val text = StringBuilder()
                .append("-------------------------------------------------")
                .append("\nsessionId: ")
                .append(streamSession.sessionId)
                .append("\nstreamId: ")
                .append(streamSession.streamId)
                .append("\ntitle: ")
                .append(streamSession.title)
                .append("\nduration : ")
                .append(streamSession.watchSeconds)
                .append(" seconds")
                .append("\nstartedAt : ")
                .append(startTimeText)
                .append("\nendedAt : ")
                .append(endTimeText)
                .toString()
        holder.itemView.stream_title_textview.text = text

        if (streamSession.syncState == "synced") {
            val lastSyncText = DateFormat.format("MMMM dd yyyy, h:mm aa", streamSession.syncedAt?.millis
                    ?: 0).toString()
            holder.itemView.stream_desc_textview.text = "Status : Synced\nsyncedAt : $lastSyncText"
            holder.itemView.stream_desc_textview.setTextColor(context.resources.getColor(R.color.color_quite_green))
        } else {
            holder.itemView.stream_desc_textview.text = "Status : " + streamSession.syncState
            holder.itemView.stream_desc_textview.setTextColor(context.resources.getColor(R.color.color_quite_red))
        }
    }

    class StreamSessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun getItemCount(): Int {
        return sessionList.size
    }

}
