package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoChannelMembership
import com.ekoapp.sample.chatfeature.R
import kotlinx.android.synthetic.main.component_header_channel_membership.view.*


class HeaderChannelMembershipComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_header_channel_membership, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(item: PagedList<EkoChannelMembership>) {
        text_total_acquaintances.visibility = View.VISIBLE
        text_total_acquaintances.text = String.format(context.getString(R.string.temporarily_total_members), item.size)
    }
}