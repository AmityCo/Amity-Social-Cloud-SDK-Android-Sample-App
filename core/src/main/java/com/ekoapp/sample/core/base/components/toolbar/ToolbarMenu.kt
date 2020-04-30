package com.ekoapp.sample.core.base.components.toolbar

import android.view.Menu
import android.view.MenuItem

interface ToolbarMenu {
	fun onOptionsItemSelected(item: MenuItem?): Boolean
	fun onPrepareOptionsMenu(menu: Menu?): Boolean = true
	fun getMenu(): Int
}