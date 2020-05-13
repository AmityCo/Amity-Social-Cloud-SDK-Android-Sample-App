package com.ekoapp.sample.socialfeature.users.view

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.ekosdk.EkoUserSortOption
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import javax.inject.Inject

class UsersViewModel @Inject constructor() : DisposableViewModel() {

    fun getUserList(): LiveData<PagedList<EkoUser>> {
        return EkoClient.newUserRepository().getAllUsers(EkoUserSortOption.DISPLAYNAME)
    }
    
}