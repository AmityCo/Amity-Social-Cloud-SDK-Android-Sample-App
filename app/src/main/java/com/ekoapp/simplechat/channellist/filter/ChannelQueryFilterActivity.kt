package com.ekoapp.simplechat.channellist.filter

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.ekoapp.simplechat.BaseActivity
import com.ekoapp.simplechat.R
import com.ekoapp.simplechat.channellist.filter.channeltype.ChannelTypeFilterFragment
import com.ekoapp.simplechat.channellist.filter.channeltype.ChannelTypeFilterViewModel
import com.ekoapp.simplechat.channellist.filter.excludetags.ExcludeTagFilterFragment
import com.ekoapp.simplechat.channellist.filter.excludetags.ExcludeTagFilterViewModel
import com.ekoapp.simplechat.channellist.filter.includedeleted.IncludedDeletedFilterViewModel
import com.ekoapp.simplechat.channellist.filter.includetags.IncludeTagFilterFragment
import com.ekoapp.simplechat.channellist.filter.includetags.IncludeTagFilterViewModel
import com.ekoapp.simplechat.channellist.filter.membership.MembershipFilterFragment
import com.ekoapp.simplechat.channellist.filter.membership.MembershipFilterViewModel
import kotlinx.android.synthetic.main.activity_channel_query_filter.*

class ChannelQueryFilterActivity : BaseActivity(), ChannelQueryFilterContract.View {

    private lateinit var presenter: ChannelQueryFilterContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
        setContentView(R.layout.activity_channel_query_filter)
        attachFragments()
        setViewListener()
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
        val includedDeletedFilterViewModel = ViewModelProviders.of(this).get(IncludedDeletedFilterViewModel::class.java)

        presenter = ChannelQueryFilterPresenter(this,
                channelTypeFilterViewModel,
                membershipFilterViewModel,
                includeTagFilterViewModel,
                excludeTagFilterViewModel,
                includedDeletedFilterViewModel)
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