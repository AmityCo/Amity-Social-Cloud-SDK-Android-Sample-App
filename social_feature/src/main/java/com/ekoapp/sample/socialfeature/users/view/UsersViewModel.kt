package com.ekoapp.sample.socialfeature.users.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.ekosdk.EkoUserSortOption
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.rx.into
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.socialfeature.users.data.UserData
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UsersViewModel @Inject constructor() : DisposableViewModel() {

    private val keywordRelay = MutableLiveData<String>()

    val usersActionRelay = SingleLiveData<UserData>()

    fun observeUserPage(): SingleLiveData<UserData> = usersActionRelay
    fun observeKeyword(): LiveData<String> = keywordRelay

    fun getUserList(): LiveData<PagedList<EkoUser>> {
        return EkoClient.newUserRepository().getAllUsers(EkoUserSortOption.DISPLAYNAME)
    }

    fun getSearchUserList(keyword: String): LiveData<PagedList<EkoUser>> {
        return EkoClient.newUserRepository().searchUserByDisplayName(keyword, EkoUserSortOption.DISPLAYNAME)
    }

    fun search(keyword: Flowable<String>) {
        keyword.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keywordRelay::postValue) into disposables
    }
}