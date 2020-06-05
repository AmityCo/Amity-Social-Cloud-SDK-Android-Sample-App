package com.ekoapp.sample.chatfeature.settings.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.settings.list.viewholder.CheckboxTypeViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder

class ChannelTypesAdapter(private val context: Context,
                          private val items: ArrayList<String>) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_checkbox_type, parent, false)
        return CheckboxTypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is CheckboxTypeViewHolder -> {
                holder.bind(items[position])
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
