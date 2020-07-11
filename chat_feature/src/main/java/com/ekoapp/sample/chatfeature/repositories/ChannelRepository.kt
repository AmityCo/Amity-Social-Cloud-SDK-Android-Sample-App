package com.ekoapp.sample.chatfeature.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.*
import com.ekoapp.sample.chatfeature.data.NotificationData
import com.google.common.collect.ImmutableSet
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class ChannelRepository @Inject constructor() {

    val channelTypes = setOf(EkoChannel.Type.STANDARD,
            EkoChannel.Type.PRIVATE,
            EkoChannel.Type.BROADCAST,
            EkoChannel.Type.CONVERSATION)

    val channelFilters = setOf(EkoChannelFilter.ALL,
            EkoChannelFilter.MEMBER,
            EkoChannelFilter.NOT_MEMBER)

    fun getTotalUnreadCount(): Flowable<Int> {
        return EkoClient.newChannelRepository().getTotalUnreadCount()
    }

    fun createStandardChannel(channelId: String): Single<EkoChannel> {
        return EkoClient.newChannelRepository()
                .createChannel()
                .standardType()
                .withChannelId(channelId)
                .build()
                .create()
    }

    fun createPrivateChannel(channelId: String): Single<EkoChannel> {
        return EkoClient.newChannelRepository()
                .createChannel()
                .privateType()
                .withChannelId(channelId)
                .build()
                .create()
    }

    fun createConversation(userId: String): Single<EkoChannel> {
        return EkoClient.newChannelRepository()
                .createChannel()
                .conversationType()
                .withUserId(userId)
                .build()
                .create()
    }

    fun joinChannel(channelId: String): Completable {
        return EkoClient.newChannelRepository().joinChannel(channelId)
    }

    fun leaveChannel(channelId: String): Completable {
        return EkoClient.newChannelRepository().leaveChannel(channelId)
    }

    fun channelCollection(types: ImmutableSet<EkoChannel.Type>,
                          filter: EkoChannelFilter,
                          includingTags: EkoTags,
                          excludingTags: EkoTags): LiveData<PagedList<EkoChannel>> {

        return EkoClient.newChannelRepository().getChannelCollection()
                .byTypes()
                .types(types)
                .filter(filter)
                .includingTags(includingTags)
                .excludingTags(excludingTags)
                .build()
                .query()
    }

    fun setTags(channelId: String, tags: EkoTags): Completable {
        return EkoClient.newChannelRepository().setTags(channelId, tags)
    }

    fun notification(channelId: String): Single<NotificationData> {
        return EkoClient.newChannelRepository().notification(channelId)
                .isAllowed
                .map { NotificationData(channelId, it) }
    }

    fun setNotification(channelId: String, isAllowed: Boolean): Completable {
        return EkoClient.newChannelRepository().notification(channelId)
                .setAllowed(isAllowed)
    }

    fun startReading(channelId: String) = EkoClient.newChannelRepository().membership(channelId).startReading()

    fun stopReading(channelId: String) = EkoClient.newChannelRepository().membership(channelId).stopReading()

    fun getMembership(channelId: String): LiveData<PagedList<EkoChannelMembership>> {
        return EkoClient.newChannelRepository()
                .membership(channelId)
                .collection
    }

}