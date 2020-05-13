package com.ekoapp.sample.socialfeature.toolbars

import android.view.MenuItem
import com.ekoapp.sample.core.base.components.toolbar.ToolbarMenu
import com.ekoapp.sample.socialfeature.R

class CreateFeedsToolbarMenu(private val description: String?,
                             private val eventPost: (String) -> Unit,
                             private val onBackPressed: () -> Unit) : ToolbarMenu {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_post -> {
                if (!description.isNullOrEmpty()) {
                    eventPost(description)
                }
                onBackPressed()
                true
            }
            else -> false
        }
    }

    override fun getMenu(): Int {
        return R.menu.create_feeds
    }
}