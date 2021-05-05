package com.amity.sample.ascsdk.community

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.core.permission.AmityRoles
import com.amity.socialcloud.sdk.core.permission.AmityRolesFilter
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunityNotificationEvent
import com.amity.socialcloud.sdk.social.community.AmityCommunityNotificationSettings
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.OpenCommunityNotificationSettingIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_community_notification_setting.*

class CommunityNotificationSettingActivity : AppCompatActivity() {

    private var compositeDisposable = CompositeDisposable()
    private var communityId: String = ""

    private var isPostReactsNetworkEnabled = false
    private var isCommentReactedNetworkEnabled = false
    private var isCommentCreatedNetworkEnabled = false
    private var isCommentRepliedNetworkEnabled = false
    private var isPostCreatedNetworkEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_notification_setting)
        initialListener()

        communityId = OpenCommunityNotificationSettingIntent.getCommunityId(intent)
        getPushNotificationSettings(communityId)
    }

    private fun initialListener() {
        button_update.setOnClickListener {
            updatePushNotificationSettings(communityId)
        }
    }

    private fun updatePushNotificationSettings(communityId: String) {
        val isPushEventEnable = switch_all_notification.isChecked
        val eventModifiers = mutableListOf<AmityCommunityNotificationEvent.MODIFIER>()

        if (isPostReactsNetworkEnabled) {
            val isEnable = switch_post_reacts_notification.isChecked
            val modifier = if (isEnable) AmityCommunityNotificationEvent.POST_REACTED.enable() else AmityCommunityNotificationEvent.POST_REACTED.disable()
            eventModifiers.add(modifier)
        }
        if (isCommentReactedNetworkEnabled) {
            val isEnable = switch_comment_reacts_notification.isChecked
            val modifier = if (isEnable) AmityCommunityNotificationEvent.COMMENT_REACTED.enable() else AmityCommunityNotificationEvent.COMMENT_REACTED.disable()
            eventModifiers.add(modifier)
        }
        if (isCommentCreatedNetworkEnabled) {
            val isEnable = switch_comment_create_notification.isChecked
            val modifier = if (isEnable) AmityCommunityNotificationEvent.COMMENT_CREATED.enable() else AmityCommunityNotificationEvent.COMMENT_CREATED.disable()
            eventModifiers.add(modifier)
        }
        if (isCommentRepliedNetworkEnabled) {
            val isEnable = switch_comment_replies_notification.isChecked
            val modifier = if (isEnable) AmityCommunityNotificationEvent.COMMENT_REPLIED.enable() else AmityCommunityNotificationEvent.COMMENT_REPLIED.disable()
            eventModifiers.add(modifier)
        }
        if (isPostCreatedNetworkEnabled) {
            val isEnable = switch_post_create_notification.isChecked
            val rolesFilter = if (toggle_post.isChecked) AmityRolesFilter.ONLY(AmityRoles(listOf("moderator"))) else AmityRolesFilter.All
            val modifier = if (isEnable) AmityCommunityNotificationEvent.POST_CREATED.enable(rolesFilter) else AmityCommunityNotificationEvent.POST_CREATED.disable()
            eventModifiers.add(modifier)
        }

        if (isPushEventEnable) {
            val disposable = AmitySocialClient.newCommunityRepository()
                    .notification(communityId)
                    .enable(eventModifiers)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        showToast("Update succeeded")
                    }, {
                        showToast(it.message ?: "Update Fail")
                    })
            compositeDisposable.add(disposable)
        } else {
            val disposable = AmitySocialClient.newCommunityRepository()
                    .notification(communityId)
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

    private fun getPushNotificationSettings(communityId: String) {
        progressbar.visibility = View.VISIBLE
        scroll_view.visibility = View.GONE
        val disposable = AmitySocialClient.newCommunityRepository()
                .notification(communityId)
                .getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ communityNotification ->
                    setCommunityPushNotification(communityNotification)
                    scroll_view.visibility = View.VISIBLE
                    progressbar.visibility = View.GONE
                }, {
                    showToast(it.message ?: "Can't get push notification setting !")
                })
        compositeDisposable.add(disposable)
    }

    private fun setCommunityPushNotification(communityNotification: AmityCommunityNotificationSettings) {
        switch_all_notification.isChecked = communityNotification.isEnabled()

        communityNotification.getNotificationEvents()?.forEach { notificationEvent ->
            when (notificationEvent) {
                is AmityCommunityNotificationEvent.POST_REACTED -> {
                    isPostReactsNetworkEnabled = notificationEvent.isNetworkEnabled()
                    if (isPostReactsNetworkEnabled) {
                        switch_post_reacts_notification.isChecked = notificationEvent.isEnabled()
                    } else {
                        view_post_reacts.visibility = View.GONE
                    }
                }
                is AmityCommunityNotificationEvent.COMMENT_REACTED -> {
                    isCommentReactedNetworkEnabled = notificationEvent.isNetworkEnabled()
                    if (isCommentReactedNetworkEnabled) {
                        switch_comment_reacts_notification.isChecked = notificationEvent.isEnabled()
                    } else {
                        view_comment_reacts.visibility = View.GONE
                    }
                }
                is AmityCommunityNotificationEvent.COMMENT_CREATED -> {
                    isCommentCreatedNetworkEnabled = notificationEvent.isNetworkEnabled()
                    if (isCommentCreatedNetworkEnabled) {
                        switch_comment_create_notification.isChecked = notificationEvent.isEnabled()
                    } else {
                        view_comment.visibility = View.GONE
                    }
                }
                is AmityCommunityNotificationEvent.COMMENT_REPLIED -> {
                    isCommentRepliedNetworkEnabled = notificationEvent.isNetworkEnabled()
                    if (isCommentRepliedNetworkEnabled) {
                        switch_comment_replies_notification.isChecked = notificationEvent.isEnabled()
                    } else {
                        view_comment_replies.visibility = View.GONE
                    }
                }
                is AmityCommunityNotificationEvent.POST_CREATED -> {
                    isPostCreatedNetworkEnabled = notificationEvent.isNetworkEnabled()
                    if (isPostCreatedNetworkEnabled) {
                        switch_post_create_notification.isChecked = notificationEvent.isEnabled()

                        val filter = notificationEvent.getRolesFilter()

                        val isModerator = filter is AmityRolesFilter.ONLY && filter.getRoles().contains("moderator")
                        toggle_post.isChecked = isModerator
                    } else {
                        view_post.visibility = View.GONE
                    }
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