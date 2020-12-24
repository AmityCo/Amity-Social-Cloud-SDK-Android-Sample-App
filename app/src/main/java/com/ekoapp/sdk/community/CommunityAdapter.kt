package com.ekoapp.sdk.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.adapter.EkoCommunityAdapter
import com.ekoapp.ekosdk.community.EkoCommunity
import com.ekoapp.sdk.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_community.view.*

class CommunityAdapter : EkoCommunityAdapter<CommunityAdapter.EkoCommunityViewHolder>() {

    private val onLongClickSubject = PublishSubject.create<EkoCommunity>()
    private val onClickSubject = PublishSubject.create<EkoCommunity>()

    val onLongClickFlowable: Flowable<EkoCommunity>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<EkoCommunity>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EkoCommunityViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_community, parent, false)
        return EkoCommunityViewHolder(view)
    }

    override fun onBindViewHolder(holder: EkoCommunityViewHolder, position: Int) {
        val community = getItem(position)
        if (community == null) {
            holder.itemView.community_id_textview.text = "loading..."
        } else {
            val text = StringBuilder()
                    .append("id: ")
                    .append(community.getChannelId())
                    .append("\ndisplay name: ")
                    .append(community.getDisplayName())
                    .append("\ndescription: ")
                    .append(community.getDescription())
                    .append("\nposts count: ")
                    .append(community.getPostCount())
                    .append("\nmembers count: ")
                    .append(community.getMemberCount())
                    .append("\npublic: ")
                    .append(community.isPublic())
                    .append("\nonly admin can post: ")
                    .append(community.onlyAdminCanPost())
                    .append("\nofficial: ")
                    .append(community.isOfficial())
                    .append("\njoined: ")
                    .append(community.isJoined())
                    .append("\ncategory ids: ")
                    .append(community.getCategories())
                    .append("\nisDeleted: ")
                    .append(community.isDeleted())
                    .toString()
            holder.itemView.community_id_textview.text = text

            holder.itemView.setOnClickListener {
                onClickSubject.onNext(community)
            }

            holder.itemView.setOnLongClickListener {
                onLongClickSubject.onNext(community)
                true
            }
        }
    }

    class EkoCommunityViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
