package com.ekoapp.sample.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.checkbox.isCheckPromptChecked
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.input
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.BuildConfig
import com.ekoapp.sample.R
import com.ekoapp.sample.SimplePreferences
import com.ekoapp.sample.intent.OpenChangeMetadataIntent
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

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
        val listener = object : FeatureAdapter.FeatureItemListener {
            override fun onClick(featureName: String) {
                when (featureName) {
                    Feature.CHAT.featureName -> {
                        installChatModule()
                    }
                    Feature.SOCIAL.featureName -> {
                        installSocialModule()
                    }
                }
            }
        }
        val adapter = FeatureAdapter(listener)
        feature_list_recyclerview.adapter = adapter
    }

    private fun installChatModule() {
        val splitInstallManager = SplitInstallManagerFactory.create(applicationContext)
        val request = SplitInstallRequest.newBuilder()
                .addModule("chat_feature")
                .build()

        splitInstallManager.startInstall(request)
                .addOnSuccessListener {
                    val intent = Intent()
                    intent.setClassName(BuildConfig.APPLICATION_ID, "com.ekoapp.sample.chatfeature.channellist.ChannelListActivity")
                    startActivity(intent)
                }.addOnFailureListener {
                    Log.e(MainActivity::class.java.simpleName, it.toString())
                }


    }

    private fun installSocialModule() {
        val splitInstallManager = SplitInstallManagerFactory.create(applicationContext)
        val request = SplitInstallRequest.newBuilder()
                .addModule("social_feature")
                .build()

        splitInstallManager.startInstall(request)
                .addOnSuccessListener {
                    val intent = Intent()
                    intent.setClassName(BuildConfig.APPLICATION_ID, "com.ekoapp.sample.socialfeature.userlist.UserListActivity")
                    startActivity(intent)
                }.addOnFailureListener {
                    Log.e(MainActivity::class.java.simpleName, it.toString())
                }
    }

    private fun showDialog(@StringRes title: Int, hint: CharSequence, prefill: CharSequence, allowEmptyInput: Boolean, callback: InputCallback) {
        MaterialDialog(this).show {
            title(title)
            input(inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS, hint = hint.toString(), prefill = prefill, allowEmpty = allowEmptyInput, callback = callback)
        }
    }

}