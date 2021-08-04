package com.amity.sample.ascsdk.community.recommended

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.community.CommunityAdapter
import com.amity.socialcloud.sdk.social.AmitySocialClient
import kotlinx.android.synthetic.main.activity_community_recommended.*

class CommunityRecommendedActivity : AppCompatActivity() {
    private val communityRepository by lazy { AmitySocialClient.newCommunityRepository() }
    private val adapter = CommunityAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_recommended)
        rvRecommendedCommunity.adapter = adapter

        LiveDataReactiveStreams.fromPublisher(
            communityRepository.getRecommendedCommunities()
        ).observe(this, Observer {
            adapter.submitList(it)
        })
    }
}