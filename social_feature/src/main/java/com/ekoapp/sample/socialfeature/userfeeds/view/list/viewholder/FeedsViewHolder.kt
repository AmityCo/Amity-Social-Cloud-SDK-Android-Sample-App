package com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeeds.view.renders.EkoUserFeedsRenderData
import com.ekoapp.sample.socialfeature.userfeeds.view.renders.ReactionData
import com.ekoapp.sample.socialfeature.userfeeds.view.renders.userFeedRender
import com.ekoapp.sample.socialfeature.users.data.UserData
import kotlinx.android.synthetic.main.item_feeds.view.*


data class FeedsData(val userData: UserData, val item: EkoPost, val viewModel: UserFeedsViewModel)

class FeedsViewHolder(itemView: View) : BaseViewHolder<FeedsData>(itemView) {

    override fun bind(item: FeedsData) {
        item.apply {
            EkoUserFeedsRenderData(this.item).userFeedRender(
                    header = itemView.header_feeds,
                    body = itemView.body_feeds,
                    reactionsSummary = itemView.reactions_summary,
                    footer = itemView.footer_feeds,
                    eventViewProfile = {
                        if (this.item.postedUserId != userData.userId) {
                            viewModel.usersActionRelay.postValue(it)
                        }
                    },
                    eventFavorite = {
                        viewModel.reactionFeeds(ReactionData(text = ReactionTypes.FAVORITE.text, isChecked = it, item = this.item))
                    },
                    eventReactionsSummary = {
                        viewModel.reactionsSummaryActionRelay.postValue(UserReactionData(postId = this.item.postId))
                    },
                    eventLike = {
                        viewModel.reactionFeeds(ReactionData(text = ReactionTypes.LIKE.text, isChecked = it, item = this.item))
                    },
                    eventEdit = viewModel.editFeedsActionRelay::postValue,
                    eventDelete = {
                        viewModel.deletePost(this.item)
                    })
        }
    }
}