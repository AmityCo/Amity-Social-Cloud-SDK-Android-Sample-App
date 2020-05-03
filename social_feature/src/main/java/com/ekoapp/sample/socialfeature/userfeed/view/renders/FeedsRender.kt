package com.ekoapp.sample.socialfeature.userfeed.view.renders

import android.content.Context
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.view.components.BodyFeedsComponent
import com.ekoapp.sample.socialfeature.userfeed.view.components.HeaderFeedsComponent

data class UserFeedsRenderData(val context: Context, val feeds: SampleFeedsResponse)

fun UserFeedsRenderData.userFeedRender(
        header: HeaderFeedsComponent,
        body: BodyFeedsComponent,
        eventEdit: (SampleFeedsResponse) -> Unit,
        eventDelete: (Boolean) -> Unit) {

    header.setupView(feeds)
    header.editFeeds { eventEdit.invoke(feeds) }
    header.deleteFeeds(eventDelete::invoke)

    body.setupView(feeds)
}
