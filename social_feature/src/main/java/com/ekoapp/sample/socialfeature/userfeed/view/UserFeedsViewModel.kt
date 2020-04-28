package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Context
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import javax.inject.Inject

class UserFeedsViewModel @Inject constructor(private val context: Context,
                                             private val userFeedsRepository: UserFeedsRepository) : DisposableViewModel() {

    fun getUserFeeds() = userFeedsRepository.getSampleDataFeeds(context).feeds
}