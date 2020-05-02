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

    fun submitEdit(id: String, description: String) {
        userFeedsRepository.sendEditFeeds(id, description)
                .doOnSuccess(this::updateList)
                .onErrorReturn {
                    Timber.i("${getCurrentClassAndMethodNames()} doOnError: ${it.message}")
                    UserFeedsTypeSealed.ErrorResult(it)
                }
                .subscribe()
    }

    fun submitDelete(id: String) {
        userFeedsRepository.sendDeleteFeeds(id)
                .doOnSuccess(this::updateList)
                .onErrorReturn {
                    Timber.i("${getCurrentClassAndMethodNames()} doOnError: ${it.message}")
                    UserFeedsTypeSealed.ErrorResult(it)
                }
                .subscribe()
    }

    private fun updateList(type: UserFeedsTypeSealed) {
        original.forEachIndexed { _, data ->
            when (type) {
                is UserFeedsTypeSealed.EditFeeds -> {
                }
                is UserFeedsTypeSealed.DeleteFeeds -> {
                    if (type.result.id == data.id && type.result.isDeleted) {
                        feedsItems.value?.remove(data)
                        return feedsItems.notifyObserver()
                    }
                }
            }
        }
    }
}