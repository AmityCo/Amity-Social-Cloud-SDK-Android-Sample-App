package com.ekoapp.simplechat.channellist.filter

import com.ekoapp.ekosdk.channel.EkoChannel
import com.ekoapp.ekosdk.channel.query.EkoChannelFilter
import com.ekoapp.simplechat.channellist.filter.includedeleted.IncludedDeletedFilterViewModel
import com.ekoapp.simplechat.SimplePreferences
import com.ekoapp.simplechat.channellist.filter.channeltype.ChannelTypeFilterViewModel
import com.ekoapp.simplechat.channellist.filter.excludetags.ExcludeTagFilterViewModel
import com.ekoapp.simplechat.channellist.filter.includetags.IncludeTagFilterViewModel
import com.ekoapp.simplechat.channellist.filter.membership.MembershipFilterViewModel
import com.google.common.collect.Sets

class ChannelQueryFilterPresenter(private val view: ChannelQueryFilterContract.View,
                                  private val channelTypeFilterViewModel: ChannelTypeFilterViewModel,
                                  private val membershipFilterViewModel: MembershipFilterViewModel,
                                  private val includeTagFilterViewModel: IncludeTagFilterViewModel,
                                  private val excludeTagFilterViewModel: ExcludeTagFilterViewModel,
                                  private val includeDeletedFilterViewModel: IncludedDeletedFilterViewModel) : ChannelQueryFilterContract.Presenter {

    override fun saveFilterOption() {
        saveChannelTypeOption()
        saveMembershipOption()
        saveIncludeTagOption()
        saveExcludeTagOption()
        saveIncludeDeletedOption()
        view.onSaveCompleted()
    }

    private fun saveChannelTypeOption() {
        val listOfChannelType = mutableListOf<String>()

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

        if (channelTypeFilterViewModel.isCommunityTypeSelected.value == true) {
            listOfChannelType.add(EkoChannel.Type.COMMUNITY.apiKey)
        }

        if (channelTypeFilterViewModel.isLiveTypeSelected.value == true) {
            listOfChannelType.add(EkoChannel.Type.LIVE.apiKey)
        }

        SimplePreferences.getChannelTypeOptions().set(listOfChannelType.toSet())
    }

    private fun saveMembershipOption() {
        val membershipOption = membershipFilterViewModel.selectedMembership.value
                ?: EkoChannelFilter.ALL.apiKey
        SimplePreferences.getChannelMembershipOption().set(membershipOption)
    }

    private fun saveIncludeTagOption() {
        val input = includeTagFilterViewModel.includingTags.value ?: ""
        val set = Sets.newConcurrentHashSet<String>()
        for (tag in input.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
            if (tag.length > 0) {
                set.add(tag)
            }
        }
        SimplePreferences.getIncludingChannelTags().set(set)
    }

    private fun saveExcludeTagOption() {
        val input = excludeTagFilterViewModel.excludingTags.value ?: ""
        val set = Sets.newConcurrentHashSet<String>()
        for (tag in input.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            if (tag.isNotEmpty()) {
                set.add(tag)
            }
        }
        SimplePreferences.getExcludingChannelTags().set(set)
    }

    private fun saveIncludeDeletedOption() {
        if (includeDeletedFilterViewModel.isIncludedDeletedSelected.value == true) {
            SimplePreferences.getIncludeDeletedOptions().set(true)
        } else {
            SimplePreferences.getIncludeDeletedOptions().set(false)
        }
    }
}