package com.ekoapp.sdk.channellist.filter.includedeleted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.preferences.SamplePreferences
import com.ekoapp.sdk.databinding.FragmentIncludeDeletedFilterBinding

class IncludeDeletedFilterFragment : Fragment() {

    lateinit var binding: FragmentIncludeDeletedFilterBinding
    private val viewModel: IncludedDeletedFilterViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_include_deleted_filter, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCachedValue()
    }

    private fun setCachedValue() {
        val includeDeleted = SamplePreferences.getIncludeDeletedOptions().get()
        viewModel.isIncludedDeletedSelected.postValue(includeDeleted)
    }

}


