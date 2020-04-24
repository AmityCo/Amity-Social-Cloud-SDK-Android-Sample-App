package com.ekoapp.sample.socialfeature.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.EkoObjects
import com.ekoapp.ekosdk.adapter.EkoUserAdapter
import com.ekoapp.sample.socialfeature.R
import kotlinx.android.synthetic.main.item_user.view.*

class UserListAdapter : EkoUserAdapter<UserListAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        if (EkoObjects.isProxy(user)) {
            holder.userId = null
            holder.itemView.user_textview.text = "loading..."
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
            holder.userId = user.userId
            holder.itemView.user_textview.text = text
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userId: String? = null
    }
}