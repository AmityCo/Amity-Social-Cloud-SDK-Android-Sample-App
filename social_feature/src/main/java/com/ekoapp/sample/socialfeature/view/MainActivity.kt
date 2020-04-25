package com.ekoapp.sample.socialfeature.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.view.di.DaggerSocialActivityComponent
import com.ekoapp.sample.ui.extensions.coreComponent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun initDependencyInjection() =
            DaggerSocialActivityComponent
                    .builder()
                    .coreComponent(coreComponent())
                    .build()
                    .inject(this)
}
