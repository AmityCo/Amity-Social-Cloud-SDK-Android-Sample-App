package com.amity.sample.ascsdk.channellist.filter.membership

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.amity.socialcloud.sdk.chat.channel.AmityChannelFilter
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.preferences.SamplePreferences
import com.amity.sample.ascsdk.databinding.FragmentMembershipFilterBinding
import kotlinx.android.synthetic.main.fragment_membership_filter.*

class MembershipFilterFragment : Fragment() {

    lateinit var binding: FragmentMembershipFilterBinding
    private val viewModel: MembershipFilterViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

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
        val modes = mutableListOf(AmityChannelFilter.ALL.apiKey, AmityChannelFilter.MEMBER.apiKey, AmityChannelFilter.NOT_MEMBER.apiKey)

        filter_spinner.adapter = ArrayAdapter(context,
                android.R.layout.simple_spinner_dropdown_item,
                modes)

        val selectedOption = SamplePreferences.getChannelMembershipOption().get()
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