package com.ekoapp.sample.socialfeature.userfeed.view.createfeeds

import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.view.UserFeedsRepository
import javax.inject.Inject

class CreateFeedsViewModel @Inject constructor(private val userFeedsRepository: UserFeedsRepository) : DisposableViewModel() {

    fun mockCreateFeeds(description: String): SampleFeedsResponse = userFeedsRepository.getMockCreateFeed(description)
}