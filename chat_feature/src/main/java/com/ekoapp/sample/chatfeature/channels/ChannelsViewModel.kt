package com.ekoapp.sample.chatfeature.channels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.components.CreateChannelData
import com.ekoapp.sample.chatfeature.repositories.ChannelRepository
import com.ekoapp.sample.chatfeature.repositories.UserRepository
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.preferences.PreferenceHelper
import com.ekoapp.sample.core.preferences.PreferenceHelper.channelTypes
import com.ekoapp.sample.core.preferences.PreferenceHelper.excludeTags
import com.ekoapp.sample.core.preferences.PreferenceHelper.includeTags
import com.ekoapp.sample.core.preferences.PreferenceHelper.membership
import com.ekoapp.sample.core.rx.into
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.core.ui.extensions.toLiveData
import com.google.common.collect.FluentIterable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChannelsViewModel @Inject constructor(private val context: Context,
                                            private val channelRepository: ChannelRepository,
                                            private val userRepository: UserRepository) : DisposableViewModel() {

    private val prefs: SharedPreferences = PreferenceHelper.defaultPreference(context)
    private val keywordRelay = MutableLiveData<String>()
    private val aboutActionRelay = SingleLiveData<EkoChannel>()
    private val settingsRelay = MutableLiveData<Unit>()
    private val joinChannelRelay = MutableLiveData<String>()

    fun observeSettings(): LiveData<Unit> = settingsRelay
    fun observeKeyword(): LiveData<String> = keywordRelay
    fun observeAboutPage(): SingleLiveData<EkoChannel> = aboutActionRelay
    fun observeJoinChannel(): LiveData<String> = joinChannelRelay

    init {
        keywordRelay.postValue("")
    }

    fun settingsAction() {
        settingsRelay.postValue(Unit)
    }

    fun renderAboutChannel(item: EkoChannel) {
        aboutActionRelay.postValue(item)
    }

    fun getAboutContent(item: EkoChannel): ArrayList<String> {
        val items = ArrayList<String>()
        items.add(String.format(context.getString(R.string.temporarily_about_channel_type), item.channelType).capitalize())
        items.add(String.format(context.getString(R.string.temporarily_about_channel_id), item.channelId))
        items.add(String.format(context.getString(R.string.temporarily_about_channel_name), item.displayName))
        items.add(String.format(context.getString(R.string.temporarily_about_channel_member), item.memberCount))
        items.add(String.format(context.getString(R.string.temporarily_about_channel_tags), item.tags))
        return items
    }

    fun bindTotalUnreadCount(): LiveData<Int> = channelRepository.getTotalUnreadCount().toLiveData()

    fun bindCreateChannel(item: CreateChannelData): Completable {
        return channelRepository.createChannel(item.id, item.type)
    }

    fun bindJoinChannel(channelId: String) {
        channelRepository
                .joinChannel(channelId)
                .doOnComplete {
                    joinChannelRelay.postValue(channelId)
                }
                .subscribe()
    }

    fun bindChannelCollection(action: (LiveData<PagedList<EkoChannel>>) -> Unit) {
        val types = FluentIterable.from(prefs.channelTypes)
                .transform { EkoChannel.Type.fromJson(it) }
                .toSet()

        val filter = EkoChannelFilter.fromApiKey(prefs.membership)
        val includingTags = prefs.includeTags?.let(::EkoTags) ?: EkoTags(emptySet())
        val excludingTags = prefs.excludeTags?.let(::EkoTags) ?: EkoTags(emptySet())

        action.invoke(channelRepository.channelCollection(types, filter, includingTags, excludingTags))
    }

    fun bindUsers(): LiveData<PagedList<EkoUser>> = userRepository.getAllUsers()

    fun bindSearchUserList(keyword: String): LiveData<PagedList<EkoUser>> {
        return userRepository.searchUserByDisplayName(keyword)
    }

    fun search(keyword: Flowable<String>) {
        keyword.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keywordRelay::postValue) into disposables
    }

    fun bindCreateConversation(userId: String) {
        channelRepository.createConversation(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

}