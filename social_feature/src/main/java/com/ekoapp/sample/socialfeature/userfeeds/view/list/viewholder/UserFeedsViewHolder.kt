package com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeeds.view.list.FeedsAdapter
import com.ekoapp.sample.socialfeature.users.data.UserData
import kotlinx.android.synthetic.main.item_user_feeds.view.*

data class UserFeedsViewData(val userData: UserData,
                             val lifecycleOwner: LifecycleOwner,
                             val viewModel: UserFeedsViewModel)

class UserFeedsViewHolder(itemView: View) : BaseViewHolder<UserFeedsViewData>(itemView) {

    private lateinit var adapter: FeedsAdapter

    override fun bind(item: UserFeedsViewData) {
        val context = itemView.context
        context.renderUserFeedsList(item)
        item.viewModel.bindUserFeeds(item.userData).observeNotNull(item.lifecycleOwner, adapter::submitList)
    }

    private fun Context.renderUserFeedsList(item: UserFeedsViewData) {
        adapter = FeedsAdapter(context = this, viewModel = item.viewModel)
        RecyclerBuilder(context = this, recyclerView = itemView.recycler_user_feeds)
                .builder()
                .build(adapter)
    }
}