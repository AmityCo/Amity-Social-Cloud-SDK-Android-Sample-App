package com.amity.sample.ascsdk.channellist.filter.excludetags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.preferences.SamplePreferences
import com.amity.sample.ascsdk.databinding.FragmentExcludeTagsFilterBinding
import com.google.common.base.Joiner

class ExcludeTagFilterFragment : Fragment() {

    lateinit var binding: FragmentExcludeTagsFilterBinding
    private val viewModel: ExcludeTagFilterViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exclude_tags_filter, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCachedValue()
    }

    private fun setCachedValue() {
        val tags = Joiner.on(",").join(SamplePreferences.getExcludingChannelTags().get())
        viewModel.excludingTags.postValue(tags)
    }

}