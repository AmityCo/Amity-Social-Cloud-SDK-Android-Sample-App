package com.ekoapp.sample.chatfeature.membership.view

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoChannelMembership
import com.ekoapp.sample.chatfeature.data.ChannelData
import com.ekoapp.sample.chatfeature.repositories.ChannelRepository
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import javax.inject.Inject

class MembershipViewModel @Inject constructor(private val channelRepository: ChannelRepository) : DisposableViewModel() {

    private var channelDataIntent: ChannelData? = null

    fun getIntentChannelData(actionRelay: (ChannelData) -> Unit) {
        channelDataIntent?.let(actionRelay::invoke)
    }

    fun setupIntent(data: ChannelData?) {
        channelDataIntent = data
    }

    fun bindGetMembership(channelId: String): LiveData<PagedList<EkoChannelMembership>> {
        return channelRepository.getMembership(channelId)
    }

}