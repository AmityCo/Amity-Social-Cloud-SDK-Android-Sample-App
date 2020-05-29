package com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder

import android.content.Context
import android.view.View
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.list.TRIPLE_SPACE
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeeds.view.list.FriendsFeedsAdapter
import kotlinx.android.synthetic.main.item_friends_feeds.view.*

data class FriendsViewData(
        val items: PagedList<EkoUser>,
        val actionFindUsers: () -> Unit,
        val actionSeeAllUsers: () -> Unit,
        val viewModel: UserFeedsViewModel)

class FriendsViewHolder(itemView: View) : BaseViewHolder<FriendsViewData>(itemView) {
    private lateinit var adapter: FriendsFeedsAdapter

    override fun bind(item: FriendsViewData) {
        val context = itemView.context
        itemView.text_total_friends.text = String.format(context.getString(R.string.temporarily_total_acquaintances), item.items.size)
        itemView.button_see_all_friends.setOnClickListener {
            item.actionSeeAllUsers.invoke()
        }
        itemView.text_find_friends.setOnClickListener {
            item.actionFindUsers.invoke()
        }
        context.renderList(item.items, item.viewModel)
    }

    private fun Context.renderList(item: PagedList<EkoUser>, viewModel: UserFeedsViewModel) {
        adapter = FriendsFeedsAdapter(this, viewModel = viewModel)
        RecyclerBuilder(context = this, recyclerView = itemView.recycler_friends, spaceCount = TRIPLE_SPACE)
                .builder()
                .build(adapter)
        adapter.submitList(item)
    }
}