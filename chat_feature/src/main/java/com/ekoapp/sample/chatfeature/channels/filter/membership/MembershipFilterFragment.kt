package com.ekoapp.sample.chatfeature.channels.filter.membership

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.databinding.FragmentMembershipFilterBinding
import com.ekoapp.sample.core.preferences.SimplePreferences
import kotlinx.android.synthetic.main.fragment_membership_filter.*

class MembershipFilterFragment : Fragment() {

    lateinit var binding: FragmentMembershipFilterBinding
    lateinit var viewModel: MembershipFilterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(MembershipFilterViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_membership_filter, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpSpinner()
    }

    private fun setUpSpinner() {
        val modes = mutableListOf(EkoChannelFilter.ALL.apiKey, EkoChannelFilter.MEMBER.getApiKey(), EkoChannelFilter.NOT_MEMBER.apiKey)

        filter_spinner.adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                modes)

        val selectedOption = SimplePreferences.getChannelMembershipOption().get()
        filter_spinner.setSelection(modes.indexOf(selectedOption))

        filter_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                viewModel.selectedMembership.value = modes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // do nothing
            }
        }
    }

}