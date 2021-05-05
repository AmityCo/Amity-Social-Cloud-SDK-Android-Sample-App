package com.amity.sample.ascsdk.notificationsettings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.view.StringListAdapter
import com.amity.sample.ascsdk.myuser.UserNotificationSettingActivity
import io.reactivex.android.schedulers.AndroidSchedulers
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
                AmityCoreClient.registerDeviceForPushNotification()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            Toast.makeText(this, String.format("register push for %s", AmityCoreClient.getUserId()), Toast.LENGTH_SHORT).show()
                        }
                        .subscribe()
            }
            unregisterPush -> {
                AmityCoreClient.unregisterDeviceForPushNotification(AmityCoreClient.getUserId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { Toast.makeText(this, String.format("un-register push for %s", AmityCoreClient.getUserId()), Toast.LENGTH_SHORT).show() }
                        .subscribe()
            }
            unregisterPushForAll -> {
                AmityCoreClient.unregisterDeviceForPushNotification()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { Toast.makeText(this, "un-register push for all users", Toast.LENGTH_SHORT).show() }
                        .subscribe()
            }
            notificationForCurrentUser -> {
                startActivity(Intent(this, UserNotificationSettingActivity::class.java))
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