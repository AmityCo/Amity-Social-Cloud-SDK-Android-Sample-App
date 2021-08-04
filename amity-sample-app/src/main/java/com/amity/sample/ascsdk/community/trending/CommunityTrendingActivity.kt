package com.amity.sample.ascsdk.community.trending

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.community.CommunityAdapter
import com.amity.socialcloud.sdk.social.AmitySocialClient
import kotlinx.android.synthetic.main.activity_community_trending.*

class CommunityTrendingActivity : AppCompatActivity() {
    private val communityRepository by lazy { AmitySocialClient.newCommunityRepository() }
    private val adapter = CommunityAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_trending)
        rvTrendingCommunity.adapter = adapter

        LiveDataReactiveStreams.fromPublisher(
            communityRepository.getTrendingCommunities()
        ).observe(this, Observer {
            adapter.submitList(it)
        })
    }
}