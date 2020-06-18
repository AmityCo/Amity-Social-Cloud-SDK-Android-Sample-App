package com.ekoapp.sample.chatfeature.channels.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channels.list.viewholder.AboutChannelViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder


class AboutChannelAdapter(private val context: Context, private val items: ArrayList<String>) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_about, parent, false)
        return AboutChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is AboutChannelViewHolder -> {
                holder.bind(items[position])
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
