package com.ekoapp.sample

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ekoapp.sample.core.ui.BaseActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.main.di.DaggerMainNavigationComponent
import kotlinx.android.synthetic.main.activity_bottom_navigation.*

class MainNavigationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        setUpNavigation()
    }

    override fun initDependencyInjection() {
        DaggerMainNavigationComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navHostFragment?.navController?.let { NavigationUI.setupWithNavController(bttm_nav, it) }
    }
}