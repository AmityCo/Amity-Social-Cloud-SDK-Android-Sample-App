package com.ekoapp.sample.socialfeature.userfeed.view.render

import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.view.components.BodyFeedsComponent
import com.ekoapp.sample.socialfeature.userfeed.view.components.HeaderFeedsComponent

fun SampleFeedsResponse.userFeedRender(headerFeedsComponent: HeaderFeedsComponent,
                                       bodyFeedsComponent: BodyFeedsComponent) {
    headerFeedsComponent.setupView(this)
    bodyFeedsComponent.setupView(this)
}