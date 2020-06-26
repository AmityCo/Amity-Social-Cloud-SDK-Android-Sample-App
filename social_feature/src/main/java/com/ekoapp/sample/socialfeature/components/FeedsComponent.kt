package com.ekoapp.sample.socialfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeeds.view.renders.EkoUserFeedsRenderData
import com.ekoapp.sample.socialfeature.userfeeds.view.renders.ReactionData
import com.ekoapp.sample.socialfeature.userfeeds.view.renders.userFeedRender
import kotlinx.android.synthetic.main.component_feeds.view.*


class FeedsComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_feeds, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(item: EkoPost, viewModel: UserFeedsViewModel) {
        EkoUserFeedsRenderData(item).userFeedRender(
                header = header_feeds,
                body = body_feeds,
                reactionsSummary = reactions_summary,
                footer = footer_feeds,
                eventViewProfile = viewModel.usersActionRelay::postValue,
                eventFavorite = {
                    viewModel.reactionFeeds(ReactionData(text = ReactionTypes.FAVORITE.text, isChecked = it, item = item))
                },
                eventReactionsSummary = {
                    viewModel.reactionsSummaryActionRelay.postValue(UserReactionData(postId = item.postId))
                },
                eventLike = {
                    viewModel.reactionFeeds(ReactionData(text = ReactionTypes.LIKE.text, isChecked = it, item = item))
                },
                eventEdit = viewModel.editFeedsActionRelay::postValue,
                eventDelete = {
                    viewModel.bindDeletePost(item)
                },
                eventReport = viewModel::initReportPost)
    }
}