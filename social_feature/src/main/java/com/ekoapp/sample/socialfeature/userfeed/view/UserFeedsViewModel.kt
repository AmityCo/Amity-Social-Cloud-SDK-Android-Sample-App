package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.model.SampleUserFeedsResponse
import io.reactivex.processors.PublishProcessor
import timber.log.Timber
import javax.inject.Inject

class UserFeedsViewModel @Inject constructor(context: Context,
                                             private val userFeedsRepository: UserFeedsRepository) : DisposableViewModel() {

    private val data: SampleUserFeedsResponse = userFeedsRepository.getUserFeedsFromGson(context)

    val feedsItems = MutableLiveData<MutableList<SampleFeedsResponse>>()
    val deletedRelay = PublishProcessor.create<Int>()

    init {
        feedsItems.postValue(data.feeds.toMutableList())
    }

    fun submitDelete(id: String) {
        userFeedsRepository.sendDeleteFeeds(id)
                .doOnSuccess(this::findPositionDelete)
                .subscribe()
    }

    private fun findPositionDelete(result: DeleteUserFeedsResult) {
        feedsItems.value?.forEachIndexed { index, data ->
            Timber.d(getCurrentClassAndMethodNames() + " result id " + result.id + ", data " + data.id)
            if (result.id == data.id) {
                deletedRelay.onNext(index)
            }
        }
    }
}