package com.ekoapp.sample.chatfeature.messages.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import arrow.core.extensions.list.functorFilter.filter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.data.ReactionData
import com.ekoapp.sample.chatfeature.messages.view.list.viewholder.ReactionViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.rx.into
import io.reactivex.disposables.CompositeDisposable

class SelectReactionsAdapter(private val context: Context,
                             private val items: ArrayList<ReactionData>,
                             private var selectedReaction: (String) -> Unit) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_select_reaction, parent, false)
        return ReactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ReactionViewHolder -> {
                holder.bind(items[position].icon)
                holder.selectedReaction()
                        .subscribe {
                            val item = items.filter { item -> item.icon == it }.first()
                            selectedReaction.invoke(item.name)
                        } into CompositeDisposable()
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
