package com.ekoapp.sample.socialfeature.users.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.rx.into
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.socialfeature.repository.UserRepository
import com.ekoapp.sample.socialfeature.users.data.UserData
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UsersViewModel @Inject constructor(private val userRepository: UserRepository) : DisposableViewModel() {

    private val keywordRelay = MutableLiveData<String>()
    private var userDataIntent: UserData? = null

    val usersActionRelay = SingleLiveData<UserData>()

    fun observeUserPage(): SingleLiveData<UserData> = usersActionRelay
    fun observeKeyword(): LiveData<String> = keywordRelay

    init {
        keywordRelay.postValue("")
    }

    fun getIntentUserData(actionRelay: (UserData) -> Unit) {
        userDataIntent?.let(actionRelay::invoke)
    }

    fun setupIntent(data: UserData?) {
        userDataIntent = data
    }

    fun bindUserList(): LiveData<PagedList<EkoUser>> = userRepository.getAllUsers()

    fun bindSearchUserList(keyword: String): LiveData<PagedList<EkoUser>> {
        return userRepository.searchUserByDisplayName(keyword)
    }

    fun search(keyword: Flowable<String>) {
        keyword.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keywordRelay::postValue) into disposables
    }
}