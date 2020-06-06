package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.settings.ChannelSettingsViewModel
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_settings_membership.view.*

class MembershipViewHolder(itemView: View) : BaseViewHolder<ChannelSettingsViewModel>(itemView) {

    override fun bind(item: ChannelSettingsViewModel) {
        val context = itemView.context
        context.renderSpinner(item)
    }

    private fun Context.renderSpinner(viewModel: ChannelSettingsViewModel) {
        itemView.spinner_membership.adapter = ArrayAdapter(this, R.layout.spinner_item, viewModel.getMembership())
        itemView.spinner_membership.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                viewModel.membershipType(itemView.spinner_membership.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
    }
}