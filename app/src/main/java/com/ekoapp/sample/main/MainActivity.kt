package com.ekoapp.sample.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.checkbox.isCheckPromptChecked
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.input
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.R
import com.ekoapp.sample.core.preferences.SimplePreferences
import com.ekoapp.sample.core.ui.BaseActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.chatfeature.usermetadata.OpenChangeMetadataIntent
import com.ekoapp.sample.core.ui.Feature
import com.ekoapp.sample.core.ui.FeatureAdapter
import com.ekoapp.sample.main.di.DaggerMainActivityComponent
import com.ekoapp.sample.utils.splitinstall.CHAT_DYNAMIC_FEATURE
import com.ekoapp.sample.utils.splitinstall.InstallModuleSealed
import com.ekoapp.sample.utils.splitinstall.SOCIAL_DYNAMIC_FEATURE
import com.ekoapp.sample.utils.splitinstall.SplitInstall
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Named


class MainActivity : BaseActivity() {

    @Inject
    lateinit var splitInstallManager: SplitInstallManager

    @Inject
    @Named("chat")
    lateinit var chatModuleInstallRequest: SplitInstallRequest

    @Inject
    @Named("social")
    lateinit var socialModuleInstallRequest: SplitInstallRequest

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_main)
        val appName = getString(R.string.app_name)
        toolbar.title = String.format("%s %s: %s", appName, "Eko SDK", com.ekoapp.ekosdk.sdk.BuildConfig.VERSION_NAME)
        toolbar.subtitle = String.format("%s", com.ekoapp.ekosdk.sdk.BuildConfig.EKO_HTTP_URL)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)

        val displayName = if (EkoClient.getDisplayName().isNullOrEmpty()) EkoClient.getUserId() else EkoClient.getDisplayName()
        EkoClient.registerDevice(EkoClient.getUserId(), displayName)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    Toast.makeText(this,
                            String.format("Registered as %s", EkoClient.getUserId()),
                            Toast.LENGTH_SHORT)
                            .show()

                    populateFeatureList()
                }
                .subscribe()
    }

    override fun initDependencyInjection() {
        DaggerMainActivityComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_register) {
            showDialog(R.string.register, "", EkoClient.getUserId(), false, { dialog, input ->
                val userId = input.toString()
                EkoClient.registerDevice(userId, userId).subscribe()
            })
            return true
        } else if (id == R.id.action_unregister) {
            EkoClient.unregisterDevice().subscribe()
            return true
        } else if (id == R.id.action_change_display_name) {
            showDialog(R.string.change_display_name, "", EkoClient.getDisplayName(), false, { dialog, input ->
                val displayName = input.toString()
                EkoClient.setDisplayName(displayName).subscribe()
            })
            return true
        } else if (id == R.id.action_change_metadata) {
            val metadata = EkoClient.getUserMetadata() ?: "Metadata has not been set"
            val intent = OpenChangeMetadataIntent(this)
            val context = this
            MaterialDialog(this).show {
                title(R.string.change_metadata)
                message(null, metadata.toString(), null)
                positiveButton(
                        null,
                        "Set",
                        {
                            context.startActivity(intent)
                        })
            }
            return true
        } else if (id == R.id.action_change_api_key) {
            val apiKeyStore = SimplePreferences.getApiKey()
            showDialog(R.string.change_api_key, "", apiKeyStore.get(), false, { dialog, input ->
                val newApiKey = input.toString()
                apiKeyStore.set(newApiKey)
                EkoClient.setup(newApiKey)
            })
            return true
        } else if (id == R.id.action_register_push) {
            EkoClient.registerDeviceForPushNotification()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { Toast.makeText(this, String.format("register push for %s", EkoClient.getUserId()), Toast.LENGTH_SHORT).show() }
                    .subscribe()
            return true
        } else if (id == R.id.action_unregister_push) {
            EkoClient.unregisterDeviceForPushNotification(EkoClient.getUserId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { Toast.makeText(this, String.format("un-register push for %s", EkoClient.getUserId()), Toast.LENGTH_SHORT).show() }
                    .subscribe()
            return true
        } else if (id == R.id.action_unregister_push_for_all) {
            EkoClient.unregisterDeviceForPushNotification()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { Toast.makeText(this, "un-register push for all users", Toast.LENGTH_SHORT).show() }
                    .subscribe()
            return true
        } else if (id == R.id.action_notification_for_current_user) {
            EkoClient.notification()
                    .isAllowed()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { allowed ->
                        MaterialDialog(this).show {
                            title(text = "Notification Settings")
                            checkBoxPrompt(text = "allow notification for current user", isCheckedDefault = allowed, onToggle = null)
                            positiveButton(text = "save change") {
                                EkoClient.notification()
                                        .setAllowed(isCheckPromptChecked())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe()
                            }
                            negativeButton(text = "discard")
                        }
                    }
                    .subscribe()
            return true
        } else if (id == R.id.action_notification_request_write_settings_permission) {
            // required for baidu push
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + getPackageName())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun populateFeatureList() {
        val dataSet: List<String> = Feature.values().map { feature -> feature.featureName }
        val listener = object : FeatureAdapter.FeatureItemListener {
            override fun onClick(featureName: String) {
                when (featureName) {
                    Feature.CHAT.featureName -> {
                        installModule(chatModuleInstallRequest)
                    }
                    Feature.SOCIAL.featureName -> {
                        installModule(socialModuleInstallRequest)
                    }
                }
            }
        }
        val adapter = FeatureAdapter(dataSet, listener)
        feature_list_recyclerview.adapter = adapter
    }

    private fun installModule(installRequest: SplitInstallRequest) {
        SplitInstall(this, splitInstallManager, installRequest).installModule {
            when (it) {
                is InstallModuleSealed.Installed -> {
                    when (it.data.module) {
                        CHAT_DYNAMIC_FEATURE -> {
                            val intent = Intent().setClassName(
                                    this,
                                    "com.ekoapp.sample.chatfeature.channellist.ChannelListActivity"
                            )
                            startActivity(intent)
                        }
                        SOCIAL_DYNAMIC_FEATURE -> {
                            val intent = Intent().setClassName(
                                    this,
                                    "com.ekoapp.sample.socialfeature.main.SocialMainActivity"
                            )
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun showDialog(@StringRes title: Int, hint: CharSequence, prefill: CharSequence, allowEmptyInput: Boolean, callback: InputCallback) {
        MaterialDialog(this).show {
            title(title)
            input(inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS, hint = hint.toString(), prefill = prefill, allowEmpty = allowEmptyInput, callback = callback)
        }
    }

}