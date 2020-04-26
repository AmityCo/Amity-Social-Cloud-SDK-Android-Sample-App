package com.ekoapp.sample.socialfeature.userfeed.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.EkoObjects
import com.ekoapp.ekosdk.adapter.EkoUserAdapter
import com.ekoapp.sample.socialfeature.R
import kotlinx.android.synthetic.main.item_user.view.*

class UserListAdapter(private val listener: UserItemListener) : EkoUserAdapter<UserListAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    interface UserItemListener {
        fun onClick(userId: String)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        if (EkoObjects.isProxy(user)) {
            holder.itemView.user_textview.text = "loading..."
            holder.itemView.setOnClickListener(null)
        } else {
            val text = StringBuilder()
                    .append("id: ")
                    .append(user!!.userId)
                    .append("\ndisplayname: ")
                    .append(user.displayName)
                    .append("\nflag count: ")
                    .append(user.flagCount)
                    .append("\nmetadata: ")
                    .append(user.metadata.toString())
                    .append("\ncreatedAt: ")
                    .append(user.createdAt.toString())
                    .toString()
            holder.itemView.user_textview.text = text
            holder.itemView.setOnClickListener {
                listener.onClick(user.userId)
            }
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}