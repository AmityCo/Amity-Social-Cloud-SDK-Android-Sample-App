package com.ekoapp.sdk.channellist.filter.channeltype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ekoapp.ekosdk.channel.EkoChannel
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.preferences.SamplePreferences
import com.ekoapp.sdk.databinding.FragmentChannelTypeFilterBinding

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

        if (channelTypes.contains(EkoChannel.Type.STANDARD.apiKey)) {
            viewModel.isStandardTypeSelected.postValue(true)
        }

        if (channelTypes.contains(EkoChannel.Type.PRIVATE.apiKey)) {
            viewModel.isPrivateTypeSelected.postValue(true)
        }

        if (channelTypes.contains(EkoChannel.Type.BROADCAST.apiKey)) {
            viewModel.isBroadcastTypeSelected.postValue(true)
        }

        if (channelTypes.contains(EkoChannel.Type.CONVERSATION.apiKey)) {
            viewModel.isChatTypeSelected.postValue(true)
        }

        if (channelTypes.contains(EkoChannel.Type.COMMUNITY.apiKey)) {
            viewModel.isCommunityTypeSelected.postValue(true)
        }

        if (channelTypes.contains(EkoChannel.Type.LIVE.apiKey)) {
            viewModel.isLiveTypeSelected.postValue(true)
        }

    }

}


