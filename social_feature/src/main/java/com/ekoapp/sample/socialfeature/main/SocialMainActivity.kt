package com.ekoapp.sample.socialfeature.main

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.ekoapp.sample.core.ui.BaseActivity
import com.ekoapp.sample.core.ui.FeatureAdapter
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.userlist.UserListActivity
import kotlinx.android.synthetic.main.activity_social_main.*

class SocialMainActivity : BaseActivity() {

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_social_main)
        toolbar.title = getString(R.string.toolbar_social_main)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)
        populateFeatureList()
    }

    private fun populateFeatureList() {
        val dataSet: List<String> = SocialFeature.values().map { feature -> feature.featureName }
        val listener = object : FeatureAdapter.FeatureItemListener {
            override fun onClick(featureName: String) {
                when (featureName) {
                    SocialFeature.USER_FEED.featureName -> {
                        startActivity(Intent(this@SocialMainActivity, UserListActivity::class.java))
                    }
                    SocialFeature.CHANNEL_FEED.featureName -> {
                        // To be implemented
                    }
                    SocialFeature.GLOBAL_FEED.featureName -> {
                        // To be implemented
                    }
                }
            }
        }
        val adapter = SocialFeatureAdapter(dataSet, listener)
        feature_list_recyclerview.adapter = adapter
    }

}
