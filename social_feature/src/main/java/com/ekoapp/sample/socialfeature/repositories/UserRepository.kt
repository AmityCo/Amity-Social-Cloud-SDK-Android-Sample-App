package com.ekoapp.sample.socialfeature.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.ekosdk.EkoUserSortOption
import javax.inject.Inject

class UserRepository @Inject constructor() {

    fun getAllUsers(): LiveData<PagedList<EkoUser>> {
        return EkoClient.newUserRepository().getAllUsers(EkoUserSortOption.DISPLAYNAME)
    }

    fun searchUserByDisplayName(keyword: String): LiveData<PagedList<EkoUser>> {
        return EkoClient.newUserRepository().searchUserByDisplayName(keyword, EkoUserSortOption.DISPLAYNAME)
    }

}