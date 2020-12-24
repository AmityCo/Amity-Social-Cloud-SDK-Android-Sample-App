package com.ekoapp.sdk.channellist.filter

import com.ekoapp.ekosdk.channel.EkoChannel
import com.ekoapp.ekosdk.channel.query.EkoChannelFilter
import com.ekoapp.sdk.channellist.filter.channeltype.ChannelTypeFilterViewModel
import com.ekoapp.sdk.channellist.filter.excludetags.ExcludeTagFilterViewModel
import com.ekoapp.sdk.channellist.filter.includedeleted.IncludedDeletedFilterViewModel
import com.ekoapp.sdk.channellist.filter.includetags.IncludeTagFilterViewModel
import com.ekoapp.sdk.channellist.filter.membership.MembershipFilterViewModel
import com.ekoapp.sdk.common.preferences.SamplePreferences
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

//        if (listOfChannelType.isEmpty()) {
//            EkoChannel.Type.values()
//                    .iterator()
//                    .forEach {
//                        listOfChannelType.add(it.apiKey)
//                    }
//        }
        SamplePreferences.getChannelTypeOptions().set(listOfChannelType.toSet())

    }

    private fun saveMembershipOption() {
        val membershipOption = membershipFilterViewModel.selectedMembership.value
                ?: EkoChannelFilter.ALL.apiKey
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