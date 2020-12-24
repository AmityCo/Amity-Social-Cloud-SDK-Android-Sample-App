package com.ekoapp.sdk.notificationsettings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.checkbox.isCheckPromptChecked
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.view.StringListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class NotificationSettingsActivity : AppCompatActivity() {

    companion object {
        const val registerPush = "Register Push"
        const val unregisterPush = "Unregister Push"
        const val unregisterPushForAll = "Unregister Push for all"
        const val notificationForCurrentUser = "Notification for current user"
        const val baiduPermission = "Baidu write permission"
    }

    private val menuItems = listOf(registerPush, unregisterPush, unregisterPushForAll, notificationForCurrentUser, baiduPermission)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val adapter = StringListAdapter(menuItems, object : StringListAdapter.StringItemListener {
            override fun onClick(text: String) {
                onMenuItemSelected(text)
            }
        })
        menu_list.adapter = adapter
    }

    private fun onMenuItemSelected(item: String) {
        when (item) {
            registerPush -> {
                EkoClient.registerDeviceForPushNotification()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            Toast.makeText(this, String.format("register push for %s", EkoClient.getUserId()), Toast.LENGTH_SHORT).show()
                        }
                        .subscribe()
            }
            unregisterPush -> {
                EkoClient.unregisterDeviceForPushNotification(EkoClient.getUserId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { Toast.makeText(this, String.format("un-register push for %s", EkoClient.getUserId()), Toast.LENGTH_SHORT).show() }
                        .subscribe()
            }
            unregisterPushForAll -> {
                EkoClient.unregisterDeviceForPushNotification()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { Toast.makeText(this, "un-register push for all users", Toast.LENGTH_SHORT).show() }
                        .subscribe()
            }
            notificationForCurrentUser -> {
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
            }
            baiduPermission -> {
                // required for baidu push
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    intent.data = Uri.parse("package:" + getPackageName())
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
    }

}