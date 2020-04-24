package com.ekoapp.sample.socialfeature.view

import android.content.Context
import android.os.Bundle
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.di.AppContext
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    @AppContext
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println(context.applicationInfo)
    }
}
