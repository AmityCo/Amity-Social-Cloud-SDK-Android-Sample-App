package com.ekoapp.sdk.community.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sdk.R
import kotlinx.android.synthetic.main.activity_community_list.*

class CommunityCategoryList : AppCompatActivity() {

    private val communityRepository by lazy { EkoClient.newCommunityRepository() }
    private val adapter by lazy { CommunityCategoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_list)
        setUpRecycleView()
        LiveDataReactiveStreams.fromPublisher(
                communityRepository.getAllCategories()
                        .includeDeleted(false)
                        .build()
                        .query())
                .observe(this, Observer {
                    adapter.submitList(it)
                })
    }

    private fun setUpRecycleView() {
        community_list_recyclerview.adapter = adapter
    }

}