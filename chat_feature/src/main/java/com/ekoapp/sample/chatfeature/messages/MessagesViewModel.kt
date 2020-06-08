package com.ekoapp.sample.chatfeature.messages

import android.content.Context
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.sample.chatfeature.data.NotificationData
import com.ekoapp.sample.chatfeature.repositories.ChannelRepository
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.ui.extensions.toLiveData
import com.google.common.collect.Sets
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MessagesViewModel @Inject constructor(private val context: Context,
                                            private val channelRepository: ChannelRepository) : DisposableViewModel() {

    private val notificationRelay = PublishProcessor.create<NotificationData>()

    fun observeNotification() = notificationRelay.toLiveData()

    fun bindSetTags(channelId: String, tags: String) {
        val set = Sets.newConcurrentHashSet<String>()
        for (tag in tags.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            if (tag.isNotEmpty()) set.add(tag)
        }
        channelRepository.setTags(channelId, EkoTags(set))
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun bindNotification(channelId: String) {
        channelRepository.notification(channelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(notificationRelay::onNext)
                .subscribe()
    }

    fun bindSetNotification(data: NotificationData) {
        channelRepository.setNotification(data.channelId, data.isAllowed)
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

}