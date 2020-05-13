package com.ekoapp.sample.socialfeature.users.view

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.ekosdk.EkoUserSortOption
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.socialfeature.users.data.UserData
import javax.inject.Inject

class UsersViewModel @Inject constructor() : DisposableViewModel() {

    val usersActionRelay = SingleLiveData<UserData>()

    fun observeUserPage(): SingleLiveData<UserData> = usersActionRelay

    fun getUserList(): LiveData<PagedList<EkoUser>> {
        return EkoClient.newUserRepository().getAllUsers(EkoUserSortOption.DISPLAYNAME)
    }

}