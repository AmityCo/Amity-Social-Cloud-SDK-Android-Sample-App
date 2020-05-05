package com.ekoapp.sample

import androidx.navigation.dynamicfeatures.fragment.DynamicNavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.di.DaggerMainNavigationComponent
import com.ekoapp.sample.viewmodel.MainNavigationViewModel
import com.google.android.play.core.splitinstall.SplitInstallRequest
import kotlinx.android.synthetic.main.activity_bottom_navigation.*
import javax.inject.Inject
import javax.inject.Named

class MainNavigationActivity : SingleViewModelActivity<MainNavigationViewModel>() {

    @Inject
    @Named("social")
    lateinit var socialRequest: SplitInstallRequest

    override fun bindViewModel(viewModel: MainNavigationViewModel) {
        setupAppBar()
        viewModel.installModule(socialRequest)
        setUpNavigation()
    }

    private fun setupAppBar() {
        appbar_main_navigation.setTitle(getString(R.string.app_name))
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as DynamicNavHostFragment
        navHostFragment.findNavController().let { NavigationUI.setupWithNavController(bottom_navigation, it) }
    }

    override fun getViewModelClass(): Class<MainNavigationViewModel> {
        return MainNavigationViewModel::class.java
    }

    override fun getLayout(): Int {
        return R.layout.activity_bottom_navigation
    }

    override fun initDependencyInjection() {
        DaggerMainNavigationComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }
}