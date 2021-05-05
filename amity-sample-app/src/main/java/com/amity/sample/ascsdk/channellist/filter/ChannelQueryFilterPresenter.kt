package com.amity.sample.ascsdk.channellist.filter

import com.amity.socialcloud.sdk.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.chat.channel.AmityChannelFilter
import com.amity.sample.ascsdk.channellist.filter.channeltype.ChannelTypeFilterViewModel
import com.amity.sample.ascsdk.channellist.filter.excludetags.ExcludeTagFilterViewModel
import com.amity.sample.ascsdk.channellist.filter.includedeleted.IncludedDeletedFilterViewModel
import com.amity.sample.ascsdk.channellist.filter.includetags.IncludeTagFilterViewModel
import com.amity.sample.ascsdk.channellist.filter.membership.MembershipFilterViewModel
import com.amity.sample.ascsdk.common.preferences.SamplePreferences
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
            listOfChannelType.add(AmityChannel.Type.STANDARD.apiKey)
        }

        if (channelTypeFilterViewModel.isPrivateTypeSelected.value == true) {
            listOfChannelType.add(AmityChannel.Type.PRIVATE.apiKey)
        }

        if (channelTypeFilterViewModel.isBroadcastTypeSelected.value == true) {
            listOfChannelType.add(AmityChannel.Type.BROADCAST.apiKey)
        }

        if (channelTypeFilterViewModel.isChatTypeSelected.value == true) {
            listOfChannelType.add(AmityChannel.Type.CONVERSATION.apiKey)
        }

        if (channelTypeFilterViewModel.isCommunityTypeSelected.value == true) {
            listOfChannelType.add(AmityChannel.Type.COMMUNITY.apiKey)
        }

        if (channelTypeFilterViewModel.isLiveTypeSelected.value == true) {
            listOfChannelType.add(AmityChannel.Type.LIVE.apiKey)
        }

        SamplePreferences.getChannelTypeOptions().set(listOfChannelType.toSet())

    }

    private fun saveMembershipOption() {
        val membershipOption = membershipFilterViewModel.selectedMembership.value
                ?: AmityChannelFilter.ALL.apiKey
        SamplePreferences.getChannelMembershipOption().set(membershipOption)
    }

    private fun saveIncludeTagOption() {
        val input = includeTagFilterViewModel.includingTags.value ?: ""
        val set = Sets.newConcurrentHashSet<String>()
        for (tag in input.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
            if (tag.length > 0) {
                set.add(tag)
            }
        }
        SamplePreferences.getIncludingChannelTags().set(set)
    }

    private fun saveExcludeTagOption() {
        val input = excludeTagFilterViewModel.excludingTags.value ?: ""
        val set = Sets.newConcurrentHashSet<String>()
        for (tag in input.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
            if (tag.length > 0) {
                set.add(tag)
            }
        }
        SamplePreferences.getExcludingChannelTags().set(set)
    }

    private fun saveIncludeDeletedOption() {
        if (includeDeletedFilterViewModel.isIncludedDeletedSelected.value == true) {
            SamplePreferences.getIncludeDeletedOptions().set(true)
        } else {
            SamplePreferences.getIncludeDeletedOptions().set(false)
        }
    }

}