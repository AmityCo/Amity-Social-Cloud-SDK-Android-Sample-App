package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.settings.ChannelSettingsViewModel
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.preferences.SimplePreferences
import kotlinx.android.synthetic.main.item_settings_membership.view.*

class MembershipViewHolder(itemView: View) : BaseViewHolder<ChannelSettingsViewModel>(itemView) {

    override fun bind(item: ChannelSettingsViewModel) {
        val context = itemView.context
        context.setUpSpinner(item)
    }

    private fun Context.setUpSpinner(viewModel: ChannelSettingsViewModel) {
        val modes = mutableListOf(
                EkoChannelFilter.ALL.apiKey.capitalize(),
                EkoChannelFilter.MEMBER.apiKey.capitalize(),
                EkoChannelFilter.NOT_MEMBER.apiKey.capitalize())

        itemView.spinner_membership.adapter = ArrayAdapter(this,
                R.layout.spinner_item,
                modes)

        val selectedOption = SimplePreferences.getChannelMembershipOption().get()
        itemView.spinner_membership.setSelection(modes.indexOf(selectedOption))

        itemView.spinner_membership.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                viewModel.selectedMembership.value = modes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
    }
}