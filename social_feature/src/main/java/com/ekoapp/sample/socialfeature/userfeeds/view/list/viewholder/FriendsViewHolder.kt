package com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.list.TRIPLE_SPACE
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeeds.view.list.FriendsFeedsAdapter
import kotlinx.android.synthetic.main.item_friends_feeds.view.*

data class FriendsViewData(
        val lifecycleOwner: LifecycleOwner,
        val viewModel: UserFeedsViewModel)

class FriendsViewHolder(itemView: View) : BaseViewHolder<FriendsViewData>(itemView) {
    private lateinit var adapter: FriendsFeedsAdapter

    override fun bind(item: FriendsViewData) {
        val context = itemView.context
        item.viewModel.bindUserList().observeNotNull(item.lifecycleOwner, {
            context.setupView(it.size, item)
            context.renderList(it, item.viewModel)
        })
    }

    private fun Context.setupView(total: Int, item: FriendsViewData) {
        item.apply {
            itemView.text_total_friends.text = String.format(getString(R.string.temporarily_total_acquaintances), total)
            itemView.button_see_all_friends.setOnClickListener {
                viewModel.seeAllUsersActionRelay.postValue(Unit)
            }
            itemView.text_find_friends.setOnClickListener {
                viewModel.findUsersActionRelay.postValue(Unit)
            }
        }
    }

    private fun Context.renderList(item: PagedList<EkoUser>, viewModel: UserFeedsViewModel) {
        adapter = FriendsFeedsAdapter(this, viewModel = viewModel)
        RecyclerBuilder(context = this, recyclerView = itemView.recycler_friends, spaceCount = TRIPLE_SPACE)
                .builder()
                .build(adapter)
        adapter.submitList(item)
    }
}