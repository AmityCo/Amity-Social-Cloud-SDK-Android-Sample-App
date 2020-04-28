package com.ekoapp.sample.core.base.components.toolbar

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ekoapp.sample.core.R

fun AppCompatActivity.setupToolbar(toolbar: Toolbar, hasBack: Boolean = false) {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)
    setupBackButton(toolbar, hasBack)
}

fun AppCompatActivity.setupBackButton(toolbar: Toolbar, hasBack: Boolean = false) {
    if (hasBack) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}