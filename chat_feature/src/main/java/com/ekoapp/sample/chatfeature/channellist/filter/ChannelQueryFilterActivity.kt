package com.ekoapp.sample.chatfeature.channellist.filter

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channellist.filter.channeltype.ChannelTypeFilterFragment
import com.ekoapp.sample.chatfeature.channellist.filter.channeltype.ChannelTypeFilterViewModel
import com.ekoapp.sample.chatfeature.channellist.filter.excludetags.ExcludeTagFilterFragment
import com.ekoapp.sample.chatfeature.channellist.filter.excludetags.ExcludeTagFilterViewModel
import com.ekoapp.sample.chatfeature.channellist.filter.includetags.IncludeTagFilterFragment
import com.ekoapp.sample.chatfeature.channellist.filter.includetags.IncludeTagFilterViewModel
import com.ekoapp.sample.chatfeature.channellist.filter.membership.MembershipFilterFragment
import com.ekoapp.sample.chatfeature.channellist.filter.membership.MembershipFilterViewModel
import com.ekoapp.sample.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_channel_query_filter.*

class ChannelQueryFilterActivity : BaseActivity(), ChannelQueryFilterContract.View {

    private lateinit var presenter: ChannelQueryFilterContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
        attachFragments()
        setViewListener()
    }

    override fun getLayout(): Int {
        return R.layout.activity_channel_query_filter
    }

    override fun onSaveCompleted() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun initPresenter() {
        val channelTypeFilterViewModel = ViewModelProviders.of(this).get(ChannelTypeFilterViewModel::class.java)
        val membershipFilterViewModel = ViewModelProviders.of(this).get(MembershipFilterViewModel::class.java)
        val includeTagFilterViewModel = ViewModelProviders.of(this).get(IncludeTagFilterViewModel::class.java)
        val excludeTagFilterViewModel = ViewModelProviders.of(this).get(ExcludeTagFilterViewModel::class.java)

        presenter = ChannelQueryFilterPresenter(this,
                channelTypeFilterViewModel,
                membershipFilterViewModel,
                includeTagFilterViewModel,
                excludeTagFilterViewModel)

    }

    private fun attachFragments() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_channel_types, ChannelTypeFilterFragment())
                .replace(R.id.fragment_membership, MembershipFilterFragment())
                .replace(R.id.fragment_include_tags, IncludeTagFilterFragment())
                .replace(R.id.fragment_exclude_tags, ExcludeTagFilterFragment())
                .commit()

    }

    private fun setViewListener() {
        button_save.setOnClickListener {
            presenter.saveFilterOption()
        }
    }

}