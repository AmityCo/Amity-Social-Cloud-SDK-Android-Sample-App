package com.ekoapp.sample.chatfeature.channels

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.sample.chatfeature.components.CreateChannelData
import com.ekoapp.sample.chatfeature.repositories.ChannelRepository
import com.ekoapp.sample.chatfeature.repositories.UserRepository
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.preferences.SimplePreferences
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.core.ui.extensions.toLiveData
import com.google.common.collect.FluentIterable
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class ChannelsViewModel @Inject constructor(private val channelRepository: ChannelRepository,
                                            private val userRepository: UserRepository) : DisposableViewModel() {

    private val aboutActionRelay = SingleLiveData<EkoChannel>()

    fun observeAboutPage(): SingleLiveData<EkoChannel> = aboutActionRelay

    fun renderAboutChannel(item: EkoChannel) {
        aboutActionRelay.postValue(item)
    }

    fun bindTotalUnreadCount(): LiveData<Int> = channelRepository.getTotalUnreadCount().toLiveData()

    fun bindCreateChannel(item: CreateChannelData): Completable {
        return channelRepository.createChannel(item.id, item.type)
    }

    fun bindJoinChannel(channelId: String) {
        channelRepository.joinChannel(channelId).subscribe()
    }

    fun bindChannelCollection(): LiveData<PagedList<EkoChannel>> {
        val types = FluentIterable.from(SimplePreferences.getChannelTypeOptions().get())
                .transform { EkoChannel.Type.fromJson(it) }
                .toSet()

        val filter = EkoChannelFilter.fromApiKey(SimplePreferences.getChannelMembershipOption().get())
        val includingTags = EkoTags(SimplePreferences.getIncludingChannelTags().get())
        val excludingTags = EkoTags(SimplePreferences.getExcludingChannelTags().get())

        return channelRepository.channelCollection(types, filter, includingTags, excludingTags)
    }

    fun bindUsers(): LiveData<PagedList<EkoUser>> = userRepository.getAllUsers()

    fun bindCreateConversation(userId: String) {
        channelRepository.createConversation(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}