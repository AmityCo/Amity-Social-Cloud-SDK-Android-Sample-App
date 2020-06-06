package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.enums.MembershipType
import com.ekoapp.sample.chatfeature.settings.ChannelSettingsViewModel
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.preferences.PreferenceHelper
import com.ekoapp.sample.core.preferences.PreferenceHelper.membership
import kotlinx.android.synthetic.main.item_settings_membership.view.*

class MembershipViewHolder(itemView: View) : BaseViewHolder<ChannelSettingsViewModel>(itemView) {

    var membership: String? = null

    override fun bind(item: ChannelSettingsViewModel) {
        val context = itemView.context
        val prefs = PreferenceHelper.defaultPreference(context)
        context.renderSpinner(item)
        context.setupView(prefs)
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

    private fun Context.setupView(prefs: SharedPreferences) {
        membership = prefs.membership
        if (membership == EkoChannelFilter.NOT_MEMBER.apiKey) {
            membership = MembershipType.NOT_MEMBER.text
        }
        resources.getStringArray(R.array.Membership).forEachIndexed { index, text ->
            if (membership?.capitalize() == text) {
                itemView.spinner_membership.setSelection(index)
            }
        }
    }
}