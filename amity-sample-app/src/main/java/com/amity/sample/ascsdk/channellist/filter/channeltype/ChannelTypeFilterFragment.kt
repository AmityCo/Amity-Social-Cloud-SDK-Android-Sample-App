package com.amity.sample.ascsdk.channellist.filter.channeltype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.amity.socialcloud.sdk.chat.channel.AmityChannel
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.preferences.SamplePreferences
import com.amity.sample.ascsdk.databinding.FragmentChannelTypeFilterBinding

class ChannelTypeFilterFragment : Fragment() {

    lateinit var binding: FragmentChannelTypeFilterBinding
    private val viewModel: ChannelTypeFilterViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_channel_type_filter, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCachedValue()
    }

    private fun setCachedValue() {
        val channelTypes = SamplePreferences.getChannelTypeOptions().get()

        if (channelTypes.contains(AmityChannel.Type.STANDARD.apiKey)) {
            viewModel.isStandardTypeSelected.postValue(true)
        }

        if (channelTypes.contains(AmityChannel.Type.PRIVATE.apiKey)) {
            viewModel.isPrivateTypeSelected.postValue(true)
        }

        if (channelTypes.contains(AmityChannel.Type.BROADCAST.apiKey)) {
            viewModel.isBroadcastTypeSelected.postValue(true)
        }

        if (channelTypes.contains(AmityChannel.Type.CONVERSATION.apiKey)) {
            viewModel.isChatTypeSelected.postValue(true)
        }

        if (channelTypes.contains(AmityChannel.Type.COMMUNITY.apiKey)) {
            viewModel.isCommunityTypeSelected.postValue(true)
        }

        if (channelTypes.contains(AmityChannel.Type.LIVE.apiKey)) {
            viewModel.isLiveTypeSelected.postValue(true)
        }

    }

}


