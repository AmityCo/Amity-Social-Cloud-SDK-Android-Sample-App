package com.ekoapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.di.DaggerActivityComponent
import com.ekoapp.sample.R
import com.ekoapp.sample.ui.extensions.coreComponent
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import javax.inject.Inject


class MainNavigationActivity : AppCompatActivity() {

    @Inject
    lateinit var installManager: SplitInstallManager

    @Inject
    lateinit var installRequest: SplitInstallRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        initDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)

        val intent = Intent().setClassName(
                this,
                "com.ekoapp.sample.chatfeature.channellist.ChannelListActivity"
        )
        startActivity(intent)

    }

    private fun initDependencyInjection() =
            DaggerActivityComponent
                    .builder()
                    .coreComponent(coreComponent())
                    .build()
                    .inject(this)

}