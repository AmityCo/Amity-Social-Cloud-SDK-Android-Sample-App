package com.ekoapp.simplechat.channellist.filter.channeltype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.simplechat.R
import com.ekoapp.simplechat.SimplePreferences
import com.ekoapp.simplechat.databinding.FragmentChannelTypeFilterBinding

class ChannelTypeFilterFragment : Fragment() {

    lateinit var binding : FragmentChannelTypeFilterBinding
    lateinit var viewModel: ChannelTypeFilterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(ChannelTypeFilterViewModel::class.java)
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
        val channelTypes = SimplePreferences.getChannelTypeOptions().get()
        if(channelTypes.contains(EkoChannel.Type.STANDARD.apiKey)) {
            viewModel.isStandardTypeSelected.postValue(true)
        }

        if(channelTypes.contains(EkoChannel.Type.PRIVATE.apiKey)) {
            viewModel.isPrivateTypeSelected.postValue(true)
        }

//        if(channelTypes.contains(EkoChannel.Type.BROADCAST.apiKey)) {
//            viewModel.isBroadcastTypeSelected.postValue(true)
//        }
//
//        if(channelTypes.contains(EkoChannel.Type.CONVERSATION.apiKey)) {
//            viewModel.isChatTypeSelected.postValue(true)
//        }

    }

}


