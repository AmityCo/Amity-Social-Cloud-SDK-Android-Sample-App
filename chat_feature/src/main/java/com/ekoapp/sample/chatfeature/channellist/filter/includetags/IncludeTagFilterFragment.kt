package com.ekoapp.sample.chatfeature.channellist.filter.includetags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.SimplePreferences
import com.ekoapp.sample.chatfeature.databinding.FragmentIncludeTagsFilterBinding
import com.google.common.base.Joiner

class IncludeTagFilterFragment : Fragment() {

    lateinit var binding: FragmentIncludeTagsFilterBinding
    lateinit var viewModel: IncludeTagFilterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(IncludeTagFilterViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_include_tags_filter, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCachedValue()
    }

    private fun setCachedValue() {
        val tags = Joiner.on(",").join(SimplePreferences.getIncludingChannelTags().get())
        viewModel.includingTags.postValue(tags)
    }

}