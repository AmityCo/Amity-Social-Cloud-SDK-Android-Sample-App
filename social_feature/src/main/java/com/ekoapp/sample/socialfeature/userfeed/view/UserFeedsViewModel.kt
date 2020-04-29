package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Context
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import javax.inject.Inject

class UserFeedsViewModel @Inject constructor(private val context: Context,
                                             private val userFeedsRepository: UserFeedsRepository) : DisposableViewModel() {

    fun getUserFeeds() = userFeedsRepository.getSampleDataFeeds(context).feeds

    fun submitDeleteFeed(isDeleted: Boolean) {
        userFeedsRepository.sendDeleteFeeds(isDeleted)
    }

    fun renderDelete() = userFeedsRepository.isDeletedLive

    fun updateDeletedList(newData: SampleFeedsResponse, action: (Int) -> Unit) {
        getUserFeeds().forEachIndexed { index, oldData ->
            if (oldData.id == newData.id && newData.isDeleted) {
                action.invoke(index)
            }
        }
    }
}