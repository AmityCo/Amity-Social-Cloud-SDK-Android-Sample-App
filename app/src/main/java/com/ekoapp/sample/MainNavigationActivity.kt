package com.ekoapp.sample

import android.os.Bundle
import androidx.navigation.dynamicfeatures.fragment.DynamicNavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.main.di.DaggerMainNavigationComponent
import com.ekoapp.sample.viewmodel.MainNavigationViewModel
import com.google.android.play.core.splitinstall.SplitInstallRequest
import kotlinx.android.synthetic.main.activity_bottom_navigation.*
import javax.inject.Inject
import javax.inject.Named

class MainNavigationActivity : SingleViewModelActivity<MainNavigationViewModel>() {

    @Inject
    @Named("social")
    lateinit var socialRequest: SplitInstallRequest

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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as DynamicNavHostFragment
        navHostFragment.findNavController().let { NavigationUI.setupWithNavController(bottom_navigation, it) }
    }

    override fun bindViewModel(viewModel: MainNavigationViewModel) {
        viewModel.installModule(socialRequest)
    }

    override fun getViewModelClass(): Class<MainNavigationViewModel> {
        return MainNavigationViewModel::class.java
    }
}