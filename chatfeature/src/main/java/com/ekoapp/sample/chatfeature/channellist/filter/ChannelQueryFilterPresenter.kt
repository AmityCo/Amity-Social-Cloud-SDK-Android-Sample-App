package com.ekoapp.sample.chatfeature.channellist.filter

import android.content.Context
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.sample.chatfeature.channellist.filter.channeltype.ChannelTypeFilterViewModel
import com.ekoapp.sample.chatfeature.channellist.filter.excludetags.ExcludeTagFilterViewModel
import com.ekoapp.sample.chatfeature.channellist.filter.includetags.IncludeTagFilterViewModel
import com.ekoapp.sample.chatfeature.channellist.filter.membership.MembershipFilterViewModel
import com.ekoapp.sample.utils.PreferenceHelper.channelMembershipOption
import com.ekoapp.sample.utils.PreferenceHelper.channelTypeOptions
import com.ekoapp.sample.utils.PreferenceHelper.defaultPreference
import com.ekoapp.sample.utils.PreferenceHelper.excludingChannelTags
import com.ekoapp.sample.utils.PreferenceHelper.includingChannelTags
import com.google.common.collect.Sets

class ChannelQueryFilterPresenter(
        private val context: Context,
        private val view: ChannelQueryFilterContract.View,
        private val channelTypeFilterViewModel: ChannelTypeFilterViewModel,
        private val membershipFilterViewModel: MembershipFilterViewModel,
        private val includeTagFilterViewModel: IncludeTagFilterViewModel,
        private val excludeTagFilterViewModel: ExcludeTagFilterViewModel) : ChannelQueryFilterContract.Presenter {


    override fun saveFilterOption() {
        saveChannelTypeOption()
        saveMembershipOption()
        saveIncludeTagOption()
        saveExcludeTagOption()
        view.onSaveCompleted()
    }

    private fun saveChannelTypeOption() {
        val listOfChannelType = mutableListOf<String>()
        val set = Sets.newConcurrentHashSet<String>()

        if (channelTypeFilterViewModel.isStandardTypeSelected.value == true) {
            listOfChannelType.add(EkoChannel.Type.STANDARD.apiKey)
        }

        if (channelTypeFilterViewModel.isPrivateTypeSelected.value == true) {
            listOfChannelType.add(EkoChannel.Type.PRIVATE.apiKey)
        }

        if (channelTypeFilterViewModel.isBroadcastTypeSelected.value == true) {
            listOfChannelType.add(EkoChannel.Type.BROADCAST.apiKey)
        }

        if (channelTypeFilterViewModel.isChatTypeSelected.value == true) {
            listOfChannelType.add(EkoChannel.Type.CONVERSATION.apiKey)
        }

        if (listOfChannelType.isEmpty()) {
            EkoChannel.Type.getAllChannelTypes()
                    .iterator()
                    .forEach {
                        listOfChannelType.add(it.apiKey)
                    }
        }

        set.addAll(listOfChannelType)
        defaultPreference(context).channelTypeOptions = set

    }

    private fun saveMembershipOption() {
        val membershipOption = membershipFilterViewModel.selectedMembership.value
                ?: EkoChannelFilter.ALL.apiKey
        defaultPreference(context).channelMembershipOption = membershipOption
    }

    private fun saveIncludeTagOption() {
        val input = includeTagFilterViewModel.includingTags.value ?: ""
        val set = Sets.newConcurrentHashSet<String>()
        for (tag in input.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
            if (tag.length > 0) {
                set.add(tag)
            }
        }
        defaultPreference(context).includingChannelTags = set
    }

    private fun saveExcludeTagOption() {
        val input = excludeTagFilterViewModel.excludingTags.value ?: ""
        val set = Sets.newConcurrentHashSet<String>()
        for (tag in input.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
            if (tag.length > 0) {
                set.add(tag)
            }
        }
        defaultPreference(context).excludingChannelTags = set
    }

}