package com.ekoapp.sample.chatfeature.messages.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.data.ReactionData
import com.ekoapp.sample.chatfeature.messages.view.list.viewholder.ViewReactionViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder

class ReactionsAdapter(private val context: Context,
                       private val items: List<ReactionData>) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view_reaction, parent, false)
        return ViewReactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ViewReactionViewHolder -> {
                holder.bind(items[position].icon)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
