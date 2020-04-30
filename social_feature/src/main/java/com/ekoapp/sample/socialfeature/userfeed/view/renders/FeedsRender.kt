package com.ekoapp.sample.socialfeature.userfeed.view.renders

import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.view.components.BodyFeedsComponent
import com.ekoapp.sample.socialfeature.userfeed.view.components.HeaderFeedsComponent

fun SampleFeedsResponse.userFeedRender(
        header: HeaderFeedsComponent,
        body: BodyFeedsComponent,
        eventDelete: (Boolean) -> Unit) {

    header.setupView(this)
    header.deleteFeeds(eventDelete::invoke)

    body.setupView(this)
}