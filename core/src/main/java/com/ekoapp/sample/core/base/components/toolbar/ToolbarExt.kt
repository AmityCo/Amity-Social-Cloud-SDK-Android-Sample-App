package com.ekoapp.sample.core.base.components.toolbar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
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
            var drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_back, null)
            drawable?.apply {
                drawable = DrawableCompat.wrap(this)
                DrawableCompat.setTint(this, Color.WHITE)
                setHomeAsUpIndicator(drawable)
            }
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}