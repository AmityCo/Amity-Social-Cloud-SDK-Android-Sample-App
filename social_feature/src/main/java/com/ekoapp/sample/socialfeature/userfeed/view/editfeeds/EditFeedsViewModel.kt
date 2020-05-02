package com.ekoapp.sample.socialfeature.userfeed.view.editfeeds

import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.view.UserFeedsRepository
import javax.inject.Inject

class EditFeedsViewModel @Inject constructor(private val userFeedsRepository: UserFeedsRepository) : DisposableViewModel() {

    fun mockEditFeeds(id: String, description: String): SampleFeedsResponse = userFeedsRepository.getMockEditFeed(id, description)
}