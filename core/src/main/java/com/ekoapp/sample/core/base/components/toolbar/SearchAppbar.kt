package com.ekoapp.sample.core.base.components.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.sample.core.R
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.component_toolbar.view.*


class SearchAppbar : AppBarLayout {
    constructor(context: Context) : super(context, null as AttributeSet?) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.component_search_toolbar, this, true)
    }

    fun setup(activity: AppCompatActivity, hasBack: Boolean) {
        toolbar_main?.let {
            activity.setSupportActionBar(it)
            activity.supportActionBar?.setDisplayShowTitleEnabled(false)
            activity.setupBackButton(it, hasBack)
        }
    }
}