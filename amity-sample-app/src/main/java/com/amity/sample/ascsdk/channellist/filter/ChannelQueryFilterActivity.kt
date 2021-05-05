package com.amity.sample.ascsdk.channellist.filter

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.channellist.filter.channeltype.ChannelTypeFilterFragment
import com.amity.sample.ascsdk.channellist.filter.channeltype.ChannelTypeFilterViewModel
import com.amity.sample.ascsdk.channellist.filter.excludetags.ExcludeTagFilterFragment
import com.amity.sample.ascsdk.channellist.filter.excludetags.ExcludeTagFilterViewModel
import com.amity.sample.ascsdk.channellist.filter.includedeleted.IncludeDeletedFilterFragment
import com.amity.sample.ascsdk.channellist.filter.includedeleted.IncludedDeletedFilterViewModel
import com.amity.sample.ascsdk.channellist.filter.includetags.IncludeTagFilterFragment
import com.amity.sample.ascsdk.channellist.filter.includetags.IncludeTagFilterViewModel
import com.amity.sample.ascsdk.channellist.filter.membership.MembershipFilterFragment
import com.amity.sample.ascsdk.channellist.filter.membership.MembershipFilterViewModel
import kotlinx.android.synthetic.main.activity_channel_query_filter.*

class ChannelQueryFilterActivity : AppCompatActivity(), ChannelQueryFilterContract.View {

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
                .replace(R.id.fragment_include_deleted, IncludeDeletedFilterFragment())
                .commit()

    }

    private fun setViewListener() {
        button_save.setOnClickListener {
            presenter.saveFilterOption()
        }
    }

}