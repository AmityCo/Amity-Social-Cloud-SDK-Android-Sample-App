package com.ekoapp.utils.split

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import timber.log.Timber

data class InstallModuleData(val module: String, val isInstall: Boolean = false)

sealed class InstallModuleSealed {
    class NotInstall(val data: InstallModuleData) : InstallModuleSealed()
    class Installed(val data: InstallModuleData) : InstallModuleSealed()
}

class SplitInstall(val context: Context) {
    private val splitTag = "class : " + SplitInstall::class.java.name + " ,method: " + object {}.javaClass.enclosingMethod?.name
    private val modules = listOf(SOCIAL_DYNAMIC_FEATURE)

    private val manager: SplitInstallManager = SplitInstallManagerFactory.create(context)
    private val request: SplitInstallRequest = SplitInstallRequest
            .newBuilder()
            .addModule(SOCIAL_DYNAMIC_FEATURE)
            .build()

    fun installModule(type: (InstallModuleSealed) -> Unit) {
        modules.forEach {
            type.invoke(InstallModuleData(module = it).getInstallModuleType())
        }
    }

    private fun InstallModuleData.getInstallModuleType(): InstallModuleSealed {
        manager.installedModules.contains(module).let { isInstall ->
            if (!isInstall) {
                startInstall(module) {
                    return@startInstall InstallModuleSealed.NotInstall(InstallModuleData(module, it.isInstall))
                }
            } else return InstallModuleSealed.Installed(InstallModuleData(module, DOWNLOAD_SUCCESS))
        }

        return InstallModuleSealed.NotInstall(InstallModuleData(module, DOWNLOAD_FAILED))
    }

    private fun startInstall(module: String, result: (InstallModuleData) -> InstallModuleSealed) {
        manager.startInstall(request)
                .addOnFailureListener {
                    Timber.d("$splitTag addOnFailureListener: ${it.message}")
                    result.invoke(InstallModuleData(module = module, isInstall = DOWNLOAD_FAILED))
                }
                .addOnSuccessListener {
                    result.invoke(InstallModuleData(module = module, isInstall = DOWNLOAD_SUCCESS))
                }
                .addOnCompleteListener {
                    result.invoke(InstallModuleData(module = module, isInstall = it.isComplete))
                }
    }

}