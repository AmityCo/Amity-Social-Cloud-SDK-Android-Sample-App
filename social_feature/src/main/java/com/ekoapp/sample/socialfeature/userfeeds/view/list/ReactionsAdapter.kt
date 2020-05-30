package com.ekoapp.sample.socialfeature.userfeeds.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder.ReactionsViewHolder
import io.reactivex.processors.PublishProcessor

class ReactionsAdapter(
        private val context: Context,
        private val items: ArrayList<Int>) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private val onClick = PublishProcessor.create<Unit>()
    fun reactions() = onClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reaction, parent, false)
        return ReactionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ReactionsViewHolder -> {
                holder.bind(items[position])
                holder.itemView.setOnClickListener { onClick.onNext(Unit) }
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

