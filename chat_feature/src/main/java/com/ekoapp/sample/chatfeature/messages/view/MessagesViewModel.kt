package com.ekoapp.sample.chatfeature.messages.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.sample.chatfeature.data.ChannelData
import com.ekoapp.sample.chatfeature.data.MessageData
import com.ekoapp.sample.chatfeature.data.NotificationData
import com.ekoapp.sample.chatfeature.data.SendMessageData
import com.ekoapp.sample.chatfeature.repositories.ChannelRepository
import com.ekoapp.sample.chatfeature.repositories.MessageRepository
import com.ekoapp.sample.chatfeature.repositories.UserRepository
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.rx.into
import com.ekoapp.sample.core.ui.extensions.toLiveData
import com.ekoapp.sample.core.utils.stringToSet
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MessagesViewModel @Inject constructor(private val channelRepository: ChannelRepository,
                                            private val messageRepository: MessageRepository,
                                            private val userRepository: UserRepository) : DisposableViewModel() {

    private val textRelay = MutableLiveData<SendMessageData>()
    private val replyingRelay = MutableLiveData<EkoMessage>()
    private val afterSentRelay = MutableLiveData<Unit>()
    private val notificationRelay = PublishProcessor.create<NotificationData>()
    private var channelDataIntent: ChannelData? = null

    fun observeMessage(): LiveData<SendMessageData> = textRelay
    fun observeReplying(): LiveData<EkoMessage> = replyingRelay
    fun observeAfterSent(): LiveData<Unit> = afterSentRelay

    init {
        getIntentChannelData {
            textRelay.postValue(SendMessageData(channelId = it.channelId, text = ""))
        }
    }

    fun getIntentChannelData(actionRelay: (ChannelData) -> Unit) {
        channelDataIntent?.let(actionRelay::invoke)
    }

    fun setupIntent(data: ChannelData?) {
        channelDataIntent = data
    }

    fun renderReplying(item: EkoMessage) {
        replyingRelay.postValue(item)
    }

    fun message(item: Flowable<SendMessageData>) {
        item.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textRelay::postValue) into disposables
    }

    fun observeNotification() = notificationRelay.toLiveData()

    fun bindStartReading(channelId: String) = channelRepository.startReading(channelId)

    fun bindStopReading(channelId: String) = channelRepository.stopReading(channelId)

    fun bindGetMessageCollectionByTags(data: MessageData): LiveData<PagedList<EkoMessage>> {
        return messageRepository.getMessageCollectionByTags(data)
    }

    fun bindSendTextMessage(data: SendMessageData) {
        messageRepository.textMessage(data)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { afterSentRelay.postValue(Unit) }
                .subscribe()
    }

    fun bindSetTagsChannel(channelId: String, tags: String) {
        channelRepository.setTags(channelId, EkoTags(tags.stringToSet()))
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun bindSetTagsMessage(messageId: String, tags: String) {
        messageRepository.setTags(messageId, EkoTags(tags.stringToSet()))
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

    fun bindUnFlagMessage(messageId: String) {
        messageRepository.unFlag(messageId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun bindFlagMessage(messageId: String) {
        messageRepository.flag(messageId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun bindUnFlagUser(userId: String) {
        userRepository.unFlag(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun bindFlagUser(userId: String) {
        userRepository.flag(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}