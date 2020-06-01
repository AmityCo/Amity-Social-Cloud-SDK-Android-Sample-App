package com.ekoapp.sample.chatfeature.channels

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.sample.chatfeature.repositories.ChannelRepository
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.preferences.SimplePreferences
import com.ekoapp.sample.core.ui.extensions.toLiveData
import com.google.common.collect.FluentIterable
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class ChannelsViewModel @Inject constructor(private val channelRepository: ChannelRepository) : DisposableViewModel() {

    fun bindTotalUnreadCount(): LiveData<Int> = channelRepository.getTotalUnreadCount().toLiveData()

    fun bindCreateChannel(channelId: String, type: String): Completable {
        return channelRepository.createChannel(channelId, type)
    }

    fun bindCreateConversation(userId: String) {
        channelRepository.createConversation(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
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
}