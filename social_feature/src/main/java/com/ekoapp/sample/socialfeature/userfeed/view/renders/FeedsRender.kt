package com.ekoapp.sample.socialfeature.userfeed.view.renders

import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.view.components.BodyFeedsComponent
import com.ekoapp.sample.socialfeature.userfeed.view.components.HeaderFeedsComponent

fun SampleFeedsResponse.userFeedRender(
        headerFeedsComponent: HeaderFeedsComponent,
        bodyFeedsComponent: BodyFeedsComponent,
        onClickDelete: (Boolean) -> Unit) {

    headerFeedsComponent.setupView(this)
    headerFeedsComponent.actionDeleteFeeds(onClickDelete::invoke)

    bodyFeedsComponent.setupView(this)
}