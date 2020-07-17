package com.ekoapp.sample.chatfeature.messages.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.ekosdk.internal.data.model.EkoMessageReaction
import com.ekoapp.ekosdk.messaging.data.DataType
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.data.*
import com.ekoapp.sample.chatfeature.repositories.ChannelRepository
import com.ekoapp.sample.chatfeature.repositories.MessageRepository
import com.ekoapp.sample.chatfeature.repositories.UserRepository
import com.ekoapp.sample.core.base.list.UPPERMOST
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.rx.into
import com.ekoapp.sample.core.seals.ReportMessageSealType
import com.ekoapp.sample.core.seals.ReportSenderSealType
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.core.ui.extensions.toLiveData
import com.ekoapp.sample.core.utils.stringToSet
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MessagesViewModel @Inject constructor(private val context: Context,
                                            private val channelRepository: ChannelRepository,
                                            private val messageRepository: MessageRepository,
                                            private val userRepository: UserRepository) : DisposableViewModel() {

    private val messageRelay = MutableLiveData<SendMessageData>()
    private val clickReplyRelay = MutableLiveData<EkoMessage>()
    private val clickViewReplyRelay = SingleLiveData<ChannelData>()
    private val afterSentRelay = MutableLiveData<Unit>()
    private val reportMessageActionRelay = SingleLiveData<CharSequence>()
    private val reportSenderActionRelay = SingleLiveData<CharSequence>()
    private val notificationTitleRelay = MutableLiveData<String>()
    private val notificationRelay = PublishProcessor.create<NotificationData>()
    private var channelDataIntent: ChannelData? = null
    var replyMessageId: String? = null

    fun observeMessage(): LiveData<SendMessageData> = messageRelay
    fun observeClickReply(): LiveData<EkoMessage> = clickReplyRelay
    fun observeClickViewReply(): SingleLiveData<ChannelData> = clickViewReplyRelay
    fun observeAfterSent(): LiveData<Unit> = afterSentRelay
    fun observeReportMessage(): SingleLiveData<CharSequence> = reportMessageActionRelay
    fun observeReportSender(): SingleLiveData<CharSequence> = reportSenderActionRelay
    fun observeNotificationTitle(): LiveData<String> = notificationTitleRelay

    init {
        getIntentChannelData {
            messageRelay.postValue(SendMessageData(channelId = it.channelId, text = ""))
        }
    }

    fun getIntentChannelData(actionRelay: (ChannelData) -> Unit) {
        channelDataIntent?.let(actionRelay::invoke)
    }

    fun setupIntent(data: ChannelData?) {
        channelDataIntent = data
    }

    fun renderReplying(item: EkoMessage) {
        clickReplyRelay.postValue(item)
    }

    fun renderViewReply(item: ChannelData) {
        clickViewReplyRelay.postValue(item)
    }

    fun initMessage(item: ChannelData, message: Flowable<MessageData>) {
        message.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    SendMessageData(
                            channelId = item.channelId,
                            parentId = item.parentId,
                            messageId = replyMessageId,
                            text = it.text,
                            image = it.image,
                            file = it.file,
                            custom = it.custom)
                }
                .subscribe(messageRelay::postValue) into disposables
    }

    fun observeNotification() = notificationRelay.toLiveData()

    fun bindStartReading(channelId: String) = channelRepository.startReading(channelId)

    fun bindStopReading(channelId: String) = channelRepository.stopReading(channelId)

    fun bindGetMessageCollectionByTags(data: ChannelData): LiveData<PagedList<EkoMessage>> {
        return messageRepository.getMessageCollectionByTags(data)
    }

    fun bindSendMessage(data: SendMessageData) {
        when {
            data.text != null -> {
                messageRepository.textMessage(data)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { afterSentRelay.postValue(Unit) }
                        .subscribe()
            }
            data.image != null -> {
                messageRepository.imageMessage(data)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { afterSentRelay.postValue(Unit) }
                        .subscribe()
            }
            data.file != null -> {
                messageRepository.fileMessage(data)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { afterSentRelay.postValue(Unit) }
                        .subscribe()
            }
            data.custom != null -> {
                messageRepository.customMessage(data)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { afterSentRelay.postValue(Unit) }
                        .subscribe()
            }
            else -> {
                messageRepository.textMessage(data)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { afterSentRelay.postValue(Unit) }
                        .subscribe()
            }
        }
        replyMessageId = null
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

    fun settingNotification() {
        getIntentChannelData {
            bindGetSettingNotification(it.channelId)
        }
    }

    private fun bindGetSettingNotification(channelId: String) {
        channelRepository.notification(channelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(notificationRelay::onNext)
                .subscribe()
    }

    fun setTitleNotification() {
        getIntentChannelData {
            bindGetNotificationTitle(it.channelId)
        }
    }

    private fun bindGetNotificationTitle(channelId: String) {
        channelRepository.notification(channelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    titleNotification(it.isAllowed)
                }
                .subscribe()
    }

    fun bindSetNotification(data: NotificationData) {
        channelRepository.setNotification(data.channelId, !data.isAllowed)
                .subscribeOn(Schedulers.io())
                .doOnComplete {
                    titleNotification(!data.isAllowed)
                }
                .subscribe()
    }

    private fun titleNotification(isAllowed: Boolean) {
        if (isAllowed) {
            notificationTitleRelay.postValue(context.getString(R.string.temporarily_do_not_allow_notification))
        } else {
            notificationTitleRelay.postValue(context.getString(R.string.temporarily_allow_notification))
        }
    }

    fun bindDeleteMessage(message: EkoMessage) {
        when (DataType.from(message.type)) {
            DataType.TEXT -> {
                message.textMessageEditor?.run {
                    delete().subscribe()
                }
            }
            DataType.IMAGE -> {
                message.imageMessageEditor?.run {
                    delete().subscribe()
                }
            }
            DataType.FILE -> {
                message.fileMessageEditor?.run {
                    delete().subscribe()
                }
            }
            DataType.CUSTOM -> {
                message.customMessageEditor?.run {
                    delete().subscribe()
                }
            }
            else -> {
                message.textMessageEditor?.run {
                    delete().subscribe()
                }
            }
        }
    }

    fun initReportMessage(type: ReportMessageSealType) {
        when (type) {
            is ReportMessageSealType.FLAG -> {
                bindReportMessage(type.item.messageId)
            }
            is ReportMessageSealType.UNFLAG -> {
                bindCancelReportMessage(type.item.messageId)
            }
        }
    }

    private fun bindCancelReportMessage(messageId: String) {
        messageRepository.unFlag(messageId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    reportMessageActionRelay.postValue(context.getString(R.string.temporarily_unflag_message_success))
                }
                .doOnError {
                    reportMessageActionRelay.postValue(context.getString(R.string.temporarily_error))
                }
                .subscribe()
    }

    private fun bindReportMessage(messageId: String) {
        messageRepository.flag(messageId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    reportMessageActionRelay.postValue(context.getString(R.string.temporarily_flag_message_success))
                }
                .doOnError {
                    reportMessageActionRelay.postValue(context.getString(R.string.temporarily_error))
                }
                .subscribe()
    }

    fun initReportSender(type: ReportSenderSealType) {
        when (type) {
            is ReportSenderSealType.FLAG -> {
                bindReportSender(type.item.userId)
            }
            is ReportSenderSealType.UNFLAG -> {
                bindCancelReportSender(type.item.userId)
            }
        }
    }

    private fun bindCancelReportSender(userId: String) {
        userRepository.unFlag(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    reportSenderActionRelay.postValue(context.getString(R.string.temporarily_unflag_sender_success))
                }
                .doOnError {
                    reportSenderActionRelay.postValue(context.getString(R.string.temporarily_error))
                }
                .subscribe()
    }

    private fun bindReportSender(userId: String) {
        userRepository.flag(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    reportSenderActionRelay.postValue(context.getString(R.string.temporarily_flag_sender_success))
                }
                .doOnError {
                    reportSenderActionRelay.postValue(context.getString(R.string.temporarily_error))
                }
                .subscribe()
    }

    fun bindLeaveChannel(channelId: String) {
        channelRepository.leaveChannel(channelId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun bindGetMessageReactionCollection(messageId: String): LiveData<PagedList<EkoMessageReaction>> {
        return messageRepository.getMessageReactionCollection(messageId)
    }

    fun getReactions(): ArrayList<ReactionData> {
        val items = ArrayList<ReactionData>()
        items.add(ReactionData(name = context.getString(R.string.temporarily_emoji_love), icon = R.drawable.ic_emoji_love))
        items.add(ReactionData(name = context.getString(R.string.temporarily_emoji_sad), icon = R.drawable.ic_emoji_sad))
        items.add(ReactionData(name = context.getString(R.string.temporarily_emoji_sleep), icon = R.drawable.ic_emoji_sleep))
        items.add(ReactionData(name = context.getString(R.string.temporarily_emoji_smile), icon = R.drawable.ic_emoji_smile))
        return items
    }

    fun getScrollPosition(size: Int): Int = if (size > UPPERMOST) size - 1 else UPPERMOST
}