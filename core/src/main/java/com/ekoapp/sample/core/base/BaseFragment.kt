package com.ekoapp.sample.core.base

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.ekoapp.sample.core.base.components.toolbar.ToolbarMenu

abstract class BaseFragment : Fragment(), HasLayout {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)  //make the fragment to run onCreateOptionsMenu
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!useActivityToolbar()) {
            menu.clear()
            getToolbar()?.apply { inflater.inflate(getMenu(), menu) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return getToolbar()?.onOptionsItemSelected(item) ?: false
    }

    protected open fun getToolbar(): ToolbarMenu? = null
    protected open fun useActivityToolbar(): Boolean = false
}