package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.ui.extensions.notifyObserver
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.model.SampleUserFeedsResponse
import timber.log.Timber
import javax.inject.Inject

class UserFeedsViewModel @Inject constructor(context: Context,
                                             private val userFeedsRepository: UserFeedsRepository) : DisposableViewModel() {

    private val data: SampleUserFeedsResponse = userFeedsRepository.getUserFeedsFromGson(context)

    private val original = ArrayList<SampleFeedsResponse>()
    val feedsItems = MutableLiveData<MutableList<SampleFeedsResponse>>()

    init {
        original.addAll(data.feeds)
        feedsItems.postValue(original)
    }

    fun submitDelete(id: String) {
        userFeedsRepository.sendDeleteFeeds(id)
                .doOnSuccess(this::updateList)
                .onErrorReturn {
                    Timber.i("${getCurrentClassAndMethodNames()} doOnError: ${it.message}")
                    DeleteUserFeedsResult(id, false)
                }
                .subscribe()
    }

    private fun updateList(result: DeleteUserFeedsResult) {
        original.forEachIndexed { _, data ->
            if (result.id == data.id) {
                feedsItems.value?.remove(data)
                feedsItems.notifyObserver()
            }
        }
    }
}