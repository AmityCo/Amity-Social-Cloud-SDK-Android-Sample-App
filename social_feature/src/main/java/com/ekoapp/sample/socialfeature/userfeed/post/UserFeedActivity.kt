package com.ekoapp.sample.socialfeature.userfeed.post

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.ekoapp.sample.core.ui.BaseActivity
import com.ekoapp.sample.socialfeature.R
import kotlinx.android.synthetic.main.activity_user_list.*

class UserFeedActivity : BaseActivity() {

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_user_feed)
        toolbar.title = getString(R.string.toolbar_user_feed)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)
    }
}