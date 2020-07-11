package com.ekoapp.sample.chatfeature.toolbars

import android.view.Menu
import android.view.MenuItem
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.core.base.components.toolbar.ToolbarMenu

class MessageToolbarMenu(private val notificationMenu: (MenuItem) -> Unit,
                         private val eventMember: () -> Unit,
                         private val eventNotification: () -> Unit,
                         private val eventLeaveChannel: () -> Unit) : ToolbarMenu {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_member -> {
                eventMember.invoke()
                true
            }
            R.id.menu_notification -> {
                eventNotification.invoke()
                true
            }
            R.id.menu_leave -> {
                eventLeaveChannel.invoke()
                true
            }
            else -> false
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            notificationMenu.invoke(it.findItem(R.id.menu_notification))
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun getMenu(): Int {
        return R.menu.menu_message
    }
}