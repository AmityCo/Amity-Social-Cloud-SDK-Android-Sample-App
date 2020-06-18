package com.ekoapp.sample.chatfeature.messages.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.data.ReactionData
import com.ekoapp.sample.chatfeature.messages.view.MessagesViewModel
import com.ekoapp.sample.chatfeature.messages.view.list.viewholder.ReactionViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder

class ReactionsAdapter(private val context: Context,
                       private val items: ArrayList<ReactionData>,
                       private val viewModel: MessagesViewModel) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reaction, parent, false)
        return ReactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ReactionViewHolder -> {
                holder.bind(items[position].icon)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
