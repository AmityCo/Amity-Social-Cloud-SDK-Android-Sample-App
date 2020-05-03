package com.ekoapp.sample.socialfeature.userfeed.view.editfeeds

import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.socialfeature.userfeed.view.UserFeedsRepository
import com.ekoapp.sample.socialfeature.userfeed.view.UserFeedsTypeSealed
import timber.log.Timber
import javax.inject.Inject

class EditFeedsViewModel @Inject constructor(private val userFeedsRepository: UserFeedsRepository) : DisposableViewModel() {

    fun submitEdit(id: String, description: String, action: (UserFeedsTypeSealed) -> Unit) {
        userFeedsRepository.sendEditFeeds(id, description)
                .doOnSuccess(action::invoke)
                .onErrorReturn {
                    Timber.i("${getCurrentClassAndMethodNames()} doOnError: ${it.message}")
                    UserFeedsTypeSealed.ErrorResult(it)
                }
                .subscribe()
    }

}