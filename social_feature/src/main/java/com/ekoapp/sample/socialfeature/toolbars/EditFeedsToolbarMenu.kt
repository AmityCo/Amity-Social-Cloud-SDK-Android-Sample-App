package com.ekoapp.sample.socialfeature.toolbars

import android.view.MenuItem
import com.ekoapp.sample.core.base.components.toolbar.ToolbarMenu
import com.ekoapp.sample.socialfeature.R

class EditFeedsToolbarMenu(private val description: String?,
                           private val eventEdit: (String) -> Unit,
                           private val onBackPressed: () -> Unit) : ToolbarMenu {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_edit -> {
                if (!description.isNullOrEmpty()) {
                    eventEdit(description)
                }
                onBackPressed()
                true
            }
            else -> false
        }
    }

    override fun getMenu(): Int {
        return R.menu.edit_feeds
    }
}