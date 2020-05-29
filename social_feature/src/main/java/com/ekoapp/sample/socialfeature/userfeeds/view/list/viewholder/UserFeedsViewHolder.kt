package com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewSeal
import com.ekoapp.sample.socialfeature.userfeeds.view.list.FeedsAdapter
import kotlinx.android.synthetic.main.item_user_feeds.view.*

data class UserFeedsViewData(
        val context: Context,
        val lifecycleOwner: LifecycleOwner,
        val userFeedsViewSeal: UserFeedsViewSeal,
        val viewModel: UserFeedsViewModel)

class UserFeedsViewHolder(itemView: View) : BaseViewHolder<UserFeedsViewData>(itemView) {

    private val spaceFeeds = 1
    private lateinit var adapter: FeedsAdapter

    override fun bind(item: UserFeedsViewData) {
        val context = itemView.context
        when (item.userFeedsViewSeal) {
            is UserFeedsViewSeal.GetUserFeeds -> {
                adapter = FeedsAdapter(context = context, viewModel = item.viewModel)
                RecyclerBuilder(context = context, recyclerView = itemView.recycler_feeds, spaceCount = spaceFeeds)
                        .builder()
                        .build(adapter)
                item.renderList(items = item.userFeedsViewSeal.data)
            }
            is UserFeedsViewSeal.CreateUserFeeds -> {
                itemView.recycler_feeds.smoothScrollToPosition(item.userFeedsViewSeal.scrollToPosition)
            }
        }
    }

    private fun UserFeedsViewData.renderList(items: LiveData<PagedList<EkoPost>>) {
        items.observeNotNull(lifecycleOwner, {
            adapter.submitList(it)
        })
    }
}