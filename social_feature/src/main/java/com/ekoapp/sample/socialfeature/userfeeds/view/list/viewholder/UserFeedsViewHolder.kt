package com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder

import android.content.Context
import android.view.View
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeeds.view.list.FeedsAdapter
import kotlinx.android.synthetic.main.item_user_feeds.view.*

data class UserFeedsViewData(val items: PagedList<EkoPost>, val viewModel: UserFeedsViewModel)

class UserFeedsViewHolder(itemView: View) : BaseViewHolder<UserFeedsViewData>(itemView) {
    private lateinit var adapter: FeedsAdapter

    override fun bind(item: UserFeedsViewData) {
        val context = itemView.context
        context.renderList(items = item.items, viewModel = item.viewModel)
    }

    private fun Context.renderList(items: PagedList<EkoPost>, viewModel: UserFeedsViewModel) {
        adapter = FeedsAdapter(context = this, viewModel = viewModel)
        RecyclerBuilder(context = this, recyclerView = itemView.recycler_user_feeds)
                .builder()
                .build(adapter)
        adapter.submitList(items)
    }
}