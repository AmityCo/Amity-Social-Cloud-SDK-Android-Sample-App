package com.ekoapp.sample

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.main.di.DaggerMainNavigationComponent
import com.ekoapp.sample.viewmodel.MainNavigationViewModel
import kotlinx.android.synthetic.main.activity_bottom_navigation.*
import timber.log.Timber

class MainNavigationActivity : SingleViewModelActivity<MainNavigationViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAppBar()
        setUpNavigation()
    }

    private fun setupAppBar() {
        appbar_main_navigation.setTitle(getString(R.string.app_name))
    }

    override fun initDependencyInjection() {
        DaggerMainNavigationComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }

    override fun getLayout(): Int {
        return R.layout.activity_bottom_navigation
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment?
        navHostFragment?.navController?.let { NavigationUI.setupWithNavController(bottom_navigation, it) }
    }

    override fun bindViewModel(viewModel: MainNavigationViewModel) {
        Timber.i(getCurrentClassAndMethodNames())
    }

    override fun getViewModelClass(): Class<MainNavigationViewModel> {
        return MainNavigationViewModel::class.java
    }
}