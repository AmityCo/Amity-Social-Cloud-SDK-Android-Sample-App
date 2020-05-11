package com.ekoapp.sample

import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.core.utils.splitinstall.CHAT_DYNAMIC_FEATURE
import com.ekoapp.sample.core.utils.splitinstall.InstallModuleSealed
import com.ekoapp.sample.core.utils.splitinstall.SOCIAL_DYNAMIC_FEATURE
import com.ekoapp.sample.core.utils.splitinstall.SplitInstall
import com.google.android.play.core.splitinstall.SplitInstallRequest
import timber.log.Timber
import javax.inject.Inject

class MainNavigationViewModel @Inject constructor(private val splitInstall: SplitInstall) : DisposableViewModel() {

    fun installModule(installRequest: SplitInstallRequest) {
        splitInstall.installModule(installRequest) {
            when (it) {
                is InstallModuleSealed.Installed -> {
                    when (it.data.module) {
                        CHAT_DYNAMIC_FEATURE -> {
                            Timber.d("%s%s", getCurrentClassAndMethodNames(), CHAT_DYNAMIC_FEATURE)
                        }
                        SOCIAL_DYNAMIC_FEATURE -> {
                            Timber.d("%s%s", getCurrentClassAndMethodNames(), SOCIAL_DYNAMIC_FEATURE)
                        }
                    }
                }
            }
        }
    }

}