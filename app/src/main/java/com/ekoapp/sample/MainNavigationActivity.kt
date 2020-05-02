package com.ekoapp.sample

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.dynamicfeatures.DynamicExtras
import androidx.navigation.dynamicfeatures.DynamicInstallMonitor
import androidx.navigation.dynamicfeatures.fragment.DynamicNavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.core.utils.splitinstall.CHAT_DYNAMIC_FEATURE
import com.ekoapp.sample.core.utils.splitinstall.InstallModuleSealed
import com.ekoapp.sample.core.utils.splitinstall.SOCIAL_DYNAMIC_FEATURE
import com.ekoapp.sample.core.utils.splitinstall.SplitInstall
import com.ekoapp.sample.main.di.DaggerMainNavigationComponent
import com.ekoapp.sample.viewmodel.MainNavigationViewModel
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_bottom_navigation.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class MainNavigationActivity : SingleViewModelActivity<MainNavigationViewModel>() {

    @Inject
    lateinit var splitInstallManager: SplitInstallManager

    @Inject
    @Named("social")
    lateinit var socialRequest: SplitInstallRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAppBar()
        installModule(socialRequest)
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
        navHostFragment.findNavController().let {
            NavigationUI.setupWithNavController(bottom_navigation, it)
        }

        val installMonitor = DynamicInstallMonitor()
        if (installMonitor.isInstallRequired) {
            installMonitor.status.observe(this, object : Observer<SplitInstallSessionState> {
                override fun onChanged(sessionState: SplitInstallSessionState) {
                    when (sessionState.status()) {
                        SplitInstallSessionStatus.INSTALLED -> {
                            Timber.d("${getCurrentClassAndMethodNames()} SplitInstallSessionStatus.INSTALLED")
                            navHostFragment.findNavController().navigate(
                                    R.id.action_chatsFragment_to_feedsFragment,
                                    null,
                                    null,
                                    DynamicExtras(installMonitor)
                            )
                        }
                        SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                            Timber.d("${getCurrentClassAndMethodNames()} SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION")
                        }

                        // Handle all remaining states:
                        SplitInstallSessionStatus.FAILED -> {
                            Timber.d("${getCurrentClassAndMethodNames()} SplitInstallSessionStatus.FAILED")
                        }
                        SplitInstallSessionStatus.CANCELED -> {
                            Timber.d("${getCurrentClassAndMethodNames()} SplitInstallSessionStatus.CANCELED")
                        }
                    }

                    if (sessionState.hasTerminalStatus()) {
                        installMonitor.status.removeObserver(this);
                    }
                }
            })
        }
    }

    private fun installModule(installRequest: SplitInstallRequest) {
        SplitInstall(this, splitInstallManager, installRequest).installModule {
            when (it) {
                is InstallModuleSealed.Installed -> {
                    when (it.data.module) {
                        CHAT_DYNAMIC_FEATURE -> {

                        }
                        SOCIAL_DYNAMIC_FEATURE -> {
                            Timber.d("${getCurrentClassAndMethodNames()} SOCIAL_DYNAMIC_FEATURE")
                        }
                    }
                }
            }
        }
    }

    override fun bindViewModel(viewModel: MainNavigationViewModel) {
        Timber.i(getCurrentClassAndMethodNames())
    }

    override fun getViewModelClass(): Class<MainNavigationViewModel> {
        return MainNavigationViewModel::class.java
    }
}