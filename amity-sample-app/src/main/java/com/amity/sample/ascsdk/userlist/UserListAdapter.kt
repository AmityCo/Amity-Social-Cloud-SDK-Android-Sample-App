package com.amity.sample.ascsdk.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.extension.adapter.AmityUserAdapter
import com.amity.sample.ascsdk.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_user.view.*


class UserListAdapter : AmityUserAdapter<UserListAdapter.UserViewHolder>() {

    private val onLongClickSubject = PublishSubject.create<AmityUser>()
    private val onClickSubject = PublishSubject.create<AmityUser>()

    val onLongClickFlowable: Flowable<AmityUser>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<AmityUser>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        if (user == null) {
            holder.itemView.user_textview.text = "loading..."
        } else {
            val text = StringBuilder()
                    .append("id: ")
                    .append(user.getUserId())
                    .append("\ndisplayname: ")
                    .append(user.getDisplayName())
                    .append("\nflag count: ")
                    .append(user.getFlagCount())
                    .append("\nmetadata: ")
                    .append(user.getMetadata().toString())
                    .append("\navatarURL: ")
                    .append(user.getAvatar()?.getUrl())
                    .append("\navatarCustomURL: ")
                    .append(user.getAvatarCustomUrl())
                    .append("\ndescription: ")
                    .append(user.getDescription())
                    .append("\ncreatedAt: ")
                    .append(user.getCreatedAt().toString())
                    .toString()
            holder.itemView.user_textview.text = text
            holder.itemView.setOnClickListener {
                onClickSubject.onNext(user)
            }
            holder.itemView.setOnLongClickListener {
                onLongClickSubject.onNext(user)
                true
            }
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
