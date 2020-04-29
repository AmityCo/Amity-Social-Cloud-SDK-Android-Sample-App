package com.ekoapp.sample.socialfeature.userfeed.view

import android.view.MenuItem
import com.ekoapp.sample.core.base.components.toolbar.ToolbarMenu
import com.ekoapp.sample.socialfeature.R

class CreateFeedsToolbarMenu(private val onPostCallback: () -> Unit) : ToolbarMenu {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_post -> {
                onPostCallback()
                true
            }
            else -> false
        }
    }

    override fun getMenu(): Int {
        return R.menu.create_feeds
    }
}