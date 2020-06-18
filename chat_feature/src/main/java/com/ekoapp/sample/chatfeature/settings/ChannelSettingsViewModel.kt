package com.ekoapp.sample.chatfeature.settings

import android.content.Context
import android.content.SharedPreferences
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.enums.ChannelType
import com.ekoapp.sample.chatfeature.enums.MembershipType
import com.ekoapp.sample.chatfeature.repositories.ChannelRepository
import com.ekoapp.sample.chatfeature.settings.data.ChannelSettingsData
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.preferences.PreferenceHelper
import com.ekoapp.sample.core.preferences.PreferenceHelper.channelTypes
import com.ekoapp.sample.core.preferences.PreferenceHelper.excludeTags
import com.ekoapp.sample.core.preferences.PreferenceHelper.includeTags
import com.ekoapp.sample.core.preferences.PreferenceHelper.membership
import com.ekoapp.sample.core.ui.extensions.toLiveData
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class ChannelSettingsViewModel @Inject constructor(private val context: Context,
                                                   private val channelRepository: ChannelRepository) : DisposableViewModel() {

    private val prefs: SharedPreferences = PreferenceHelper.defaultPreference(context)
    private val typesRelay = PublishProcessor.create<Set<String>>()
    private val membershipRelay = PublishProcessor.create<String>()
    private val includeTagsRelay = PublishProcessor.create<Set<String>>()
    private val excludeTagsRelay = PublishProcessor.create<Set<String>>()
    private val saveRelay = PublishProcessor.create<Unit>()

    fun observeChannelTypes() = typesRelay.toLiveData()
    fun observeMembership() = membershipRelay.toLiveData()
    fun observeIncludeTags() = includeTagsRelay.toLiveData()
    fun observeExcludeTags() = excludeTagsRelay.toLiveData()
    fun observeSave() = saveRelay.toLiveData()

    fun channelTypes(types: Set<String>) = typesRelay.onNext(types)
    fun membershipType(types: String) = membershipRelay.onNext(mapMembership(types).apiKey)
    fun includeTags(tags: Set<String>) = includeTagsRelay.onNext(tags)
    fun excludeTags(tags: Set<String>) = excludeTagsRelay.onNext(tags)
    fun saveSettings() = saveRelay.onNext(Unit)

    fun getChannelTypes(): ArrayList<String> {
        val items = ArrayList<String>()
        items.add(ChannelType.STANDARD.text)
        items.add(ChannelType.PRIVATE.text)
        items.add(ChannelType.BROADCAST.text)
        items.add(ChannelType.CONVERSATION.text)
        return items
    }

    fun getMembership(): Array<String> = context.resources.getStringArray(R.array.Membership)

    fun mapChannelType(text: String): EkoChannel.Type {
        return channelRepository.channelTypes.first { value -> value.apiKey.contains(text, ignoreCase = true) }
    }

    private fun mapMembership(text: String): EkoChannelFilter {
        var newText = text
        if (text.decapitalize() == MembershipType.NOT_MEMBER.text) {
            newText = EkoChannelFilter.NOT_MEMBER.name
        }
        return channelRepository.channelFilters.first { value -> value.name.contains(newText, ignoreCase = true) }
    }

    fun saveChannelTypes(value: Set<String>) {
        prefs.channelTypes = value
    }

    fun saveMembership(value: String) {
        prefs.membership = value
    }

    fun saveIncludeTags(value: Set<String>) {
        prefs.includeTags = value
    }

    fun saveExcludeTags(value: Set<String>) {
        prefs.excludeTags = value
    }

    fun getChannelSettingsData(): ChannelSettingsData {
        return ChannelSettingsData(
                prefs.channelTypes ?: emptySet(),
                prefs.membership ?: EkoChannelFilter.ALL.apiKey,
                prefs.includeTags ?: emptySet(),
                prefs.excludeTags ?: emptySet())
    }
}