package com.ekoapp.sample.socialfeature.userfeeds.view.renders

import android.view.View
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.core.seals.ReportSealType
import com.ekoapp.sample.socialfeature.components.BodyFeedsComponent
import com.ekoapp.sample.socialfeature.components.FooterFeedsComponent
import com.ekoapp.sample.socialfeature.components.HeaderFeedsComponent
import com.ekoapp.sample.socialfeature.components.ReactionsSummaryFeedsComponent
import com.ekoapp.sample.socialfeature.constants.ZERO_COUNT
import com.ekoapp.sample.socialfeature.editfeeds.data.EditUserFeedsData
import com.ekoapp.sample.socialfeature.users.data.UserData

data class ReactionData(val text: String, val isChecked: Boolean, val item: EkoPost)
data class EkoUserFeedsRenderData(val item: EkoPost)

fun EkoUserFeedsRenderData.userFeedRender(header: HeaderFeedsComponent,
                                          body: BodyFeedsComponent,
                                          reactionsSummary: ReactionsSummaryFeedsComponent,
                                          footer: FooterFeedsComponent,
                                          eventViewProfile: (UserData) -> Unit,
                                          eventFavorite: (Boolean) -> Unit,
                                          eventReactionsSummary: () -> Unit,
                                          eventLike: (Boolean) -> Unit,
                                          eventEdit: (EditUserFeedsData) -> Unit,
                                          eventDelete: (Boolean) -> Unit,
                                          eventReport: (ReportSealType) -> Unit) {

    renderHeader(header, eventViewProfile, eventFavorite, eventEdit, body, eventDelete, eventReport)
    renderBody(body)
    renderReactionsSummary(reactionsSummary, eventReactionsSummary)
    renderFooter(footer, eventLike)
}

private fun EkoUserFeedsRenderData.renderFooter(footer: FooterFeedsComponent, eventLike: (Boolean) -> Unit) {
    footer.setupView(item)
    footer.likeFeeds(eventLike::invoke)
}

private fun EkoUserFeedsRenderData.renderReactionsSummary(reactionsSummary: ReactionsSummaryFeedsComponent,
                                                          eventReactionsSummary: () -> Unit) {
    if (item.reactionCount == ZERO_COUNT) {
        reactionsSummary.visibility = View.GONE
    } else {
        reactionsSummary.visibility = View.VISIBLE
        reactionsSummary.setupView(item)
        reactionsSummary.setOnClickListener { eventReactionsSummary.invoke() }
        reactionsSummary.itemsClick { eventReactionsSummary.invoke() }
    }
}

private fun EkoUserFeedsRenderData.renderBody(body: BodyFeedsComponent) {
    body.setupView(item)
}

private fun EkoUserFeedsRenderData.renderHeader(header: HeaderFeedsComponent,
                                                eventViewProfile: (UserData) -> Unit,
                                                eventFavorite: (Boolean) -> Unit,
                                                eventEdit: (EditUserFeedsData) -> Unit,
                                                body: BodyFeedsComponent,
                                                eventDelete: (Boolean) -> Unit,
                                                eventReport: (ReportSealType) -> Unit) {
    header.setupView(item)
    header.onClickFullName {
        eventViewProfile.invoke(UserData(item.postedUserId))
    }
    header.favoriteFeeds(eventFavorite::invoke)

    header.apply {
        item.setMoreHorizView(
                edit = {
                    eventEdit.invoke(
                            EditUserFeedsData(
                                    userData = UserData(userId = item.postedUserId),
                                    postId = item.postId,
                                    description = body.getDescription()))
                },
                delete = eventDelete::invoke,
                report = eventReport::invoke)
    }
}
