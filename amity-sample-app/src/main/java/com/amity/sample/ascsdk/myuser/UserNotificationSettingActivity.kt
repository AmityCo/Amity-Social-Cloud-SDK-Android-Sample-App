package com.amity.sample.ascsdk.myuser

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.permission.AmityRoles
import com.amity.socialcloud.sdk.core.permission.AmityRolesFilter

import com.amity.socialcloud.sdk.core.user.AmityUserNotificationModule
import com.amity.socialcloud.sdk.core.user.AmityUserNotificationSettings
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_notification_setting.*

class UserNotificationSettingActivity : AppCompatActivity() {
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_notification_setting)
        initialListener()
        getPushNotificationSetting()

    }

    private fun initialListener() {
        button_update?.setOnClickListener {
            updatePushNotificationSettings()
        }
    }

    private fun updatePushNotificationSettings() {
        val isPushEventEnable = switch_all_notification.isChecked
        val pushModulesModifiers = mutableListOf<AmityUserNotificationModule.MODIFIER>()
        pushModulesModifiers.add(getChatPushSettings())
        pushModulesModifiers.add(getVideoPushSettings())
        pushModulesModifiers.add(getSocialPushSettings())

        if(isPushEventEnable) {
            val disposable = AmityCoreClient
                    .notification()
                    .enable(pushModulesModifiers)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        showToast("Update succeeded")
                    }, {
                        showToast(it.message ?: "Update Fail")
                    })
            compositeDisposable.add(disposable)
        } else {
            val disposable = AmityCoreClient
                    .notification()
                    .disable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        showToast("Update succeeded")
                    }, {
                        showToast(it.message ?: "Update Fail")
                    })
            compositeDisposable.add(disposable)
        }

    }

    private fun getChatPushSettings(): AmityUserNotificationModule.MODIFIER {
        if(switch_chat_notification.isChecked) {
            val rolesFilter =  if (toggle_chat.isChecked) {
                AmityRolesFilter.ONLY(AmityRoles(listOf("moderator")))
            } else {
                AmityRolesFilter.All
            }
            return AmityUserNotificationModule.CHAT.enable(rolesFilter)
        } else {
            return AmityUserNotificationModule.CHAT.disable()
        }
    }

    private fun getVideoPushSettings(): AmityUserNotificationModule.MODIFIER {
        if(switch_video_streaming_notification.isChecked) {
            val rolesFilter =  if (toggle_video_streaming.isChecked) {
                AmityRolesFilter.ONLY(AmityRoles(listOf("moderator")))
            } else {
                AmityRolesFilter.All
            }
            return AmityUserNotificationModule.VIDEO_STREAMING.enable(rolesFilter)
        } else {
            return AmityUserNotificationModule.VIDEO_STREAMING.disable()
        }
    }

    private fun getSocialPushSettings(): AmityUserNotificationModule.MODIFIER {
        if(switch_social_notification.isChecked) {
            val rolesFilter =  if (toggle_social.isChecked) {
                AmityRolesFilter.ONLY(AmityRoles(listOf("moderator")))
            } else {
                AmityRolesFilter.All
            }
            return AmityUserNotificationModule.SOCIAL.enable(rolesFilter)
        } else {
            return AmityUserNotificationModule.SOCIAL.disable()
        }
    }

    private fun getPushNotificationSetting() {
        progressbar.visibility = View.VISIBLE
        parent_view.visibility = View.GONE
        val disposable = AmityCoreClient
                .notification()
                .getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ notification ->
                    setUserPushNotification(notification)
                    parent_view.visibility = View.VISIBLE
                    progressbar.visibility = View.GONE
                }, {
                    showToast(it.message ?: "Can't get push notification setting !")
                })
        compositeDisposable.add(disposable)
    }

    private fun setUserPushNotification(notification: AmityUserNotificationSettings) {
        switch_all_notification.isChecked = notification.isEnabled()
        notification.getModules()?.forEach { module ->
            when (module) {
                is AmityUserNotificationModule.CHAT -> {
                    switch_chat_notification.isChecked = module.isEnabled()
                    val rolesFilter = module.getRolesFilter()
                    val isModerator = rolesFilter is AmityRolesFilter.ONLY && rolesFilter.getRoles().contains("moderator")
                    toggle_chat.isChecked = isModerator
                }
                is AmityUserNotificationModule.VIDEO_STREAMING -> {
                    switch_video_streaming_notification.isChecked = module.isEnabled()
                    val rolesFilter = module.getRolesFilter()
                    val isModerator = rolesFilter is AmityRolesFilter.ONLY && rolesFilter.getRoles().contains("moderator")
                    toggle_video_streaming.isChecked = isModerator
                }
                is AmityUserNotificationModule.SOCIAL -> {
                    switch_social_notification.isChecked = module.isEnabled()
                    val rolesFilter = module.getRolesFilter()
                    val isModerator = rolesFilter is AmityRolesFilter.ONLY && rolesFilter.getRoles().contains("moderator")
                    toggle_social.isChecked = isModerator
                }
                else -> {
                }
            }
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}