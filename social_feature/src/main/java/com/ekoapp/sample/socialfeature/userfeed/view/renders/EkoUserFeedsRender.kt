package com.ekoapp.sample.socialfeature.userfeed.view.renders

import android.content.Context
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.socialfeature.userfeed.view.components.BodyFeedsComponent
import com.ekoapp.sample.socialfeature.userfeed.view.components.HeaderFeedsComponent


data class EkoUserFeedsRenderData(val context: Context, val item: EkoPost)

fun EkoUserFeedsRenderData.userFeedRender(
        header: HeaderFeedsComponent,
        body: BodyFeedsComponent,
        eventEdit: (EkoPost) -> Unit,
        eventDelete: (Boolean) -> Unit) {

    header.setupView(item)
    body.setupView(item)

    header.editFeeds { eventEdit.invoke(item) }
    header.deleteFeeds(eventDelete::invoke)
}
