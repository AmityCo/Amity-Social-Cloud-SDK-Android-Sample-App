package com.ekoapp.sample.chatfeature.channels.list.viewholder

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.sample.chatfeature.channels.ChannelsViewModel
import com.ekoapp.sample.chatfeature.channels.list.ConversationWithUserAdapter
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import kotlinx.android.synthetic.main.item_suggestion_users.view.*

data class SuggestionUsersData(
        val lifecycleOwner: LifecycleOwner,
        val viewModel: ChannelsViewModel)

class SuggestionUsersViewHolder(itemView: View) : BaseViewHolder<SuggestionUsersData>(itemView) {
    private lateinit var adapter: ConversationWithUserAdapter

    override fun bind(item: SuggestionUsersData) {
        val context = itemView.context
        adapter = ConversationWithUserAdapter(context, item.viewModel)
        RecyclerBuilder(context = context, recyclerView = itemView.recycler_suggestion_users)
                .builder()
                .build(adapter)
        item.viewModel.bindUsers().observeNotNull(item.lifecycleOwner, this::renderList)
    }

    private fun renderList(item: PagedList<EkoUser>) {
        adapter.submitList(item)
    }

}