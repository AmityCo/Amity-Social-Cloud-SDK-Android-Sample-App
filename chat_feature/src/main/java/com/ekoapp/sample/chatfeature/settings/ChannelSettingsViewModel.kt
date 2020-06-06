package com.ekoapp.sample.chatfeature.settings

import android.content.Context
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.repositories.ChannelRepository
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.ui.extensions.toLiveData
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class ChannelSettingsViewModel @Inject constructor(private val context: Context,
                                                   private val channelRepository: ChannelRepository) : DisposableViewModel() {

    private val typesRelay = PublishProcessor.create<Set<String>>()

    fun observeChannelTypes() = typesRelay.toLiveData()

    fun channelTypes(types: Set<String>) {
        typesRelay.onNext(types)
    }

    fun getChannelTypes(): ArrayList<String> {
        val items = ArrayList<String>()
        items.add(context.getString(R.string.temporarily_standard))
        items.add(context.getString(R.string.temporarily_private))
        items.add(context.getString(R.string.temporarily_broadcast))
        items.add(context.getString(R.string.temporarily_conversation))
        return items
    }

    fun getMembership(): Array<String> = context.resources.getStringArray(R.array.Membership)

    fun mapValue(text: String): EkoChannel.Type {
        return channelRepository.channelTypes.first { value -> value.apiKey.contains(text, ignoreCase = true) }
    }
}