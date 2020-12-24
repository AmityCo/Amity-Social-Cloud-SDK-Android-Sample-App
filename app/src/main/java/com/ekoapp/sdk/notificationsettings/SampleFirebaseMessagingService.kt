package com.ekoapp.sdk.notificationsettings

import android.util.Log
import com.ekoapp.push.EkoFcm
import com.ekoapp.sdk.SampleApp
import com.google.common.collect.Maps
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson


class SampleFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("fcm_message", "messageType -> " + remoteMessage.messageType)
        Log.e("fcm_message", "priority -> " + remoteMessage.priority)
        val notification: MutableMap<String, String> = Maps.newConcurrentMap()
        if (remoteMessage.notification != null) {
            notification["title"] = remoteMessage.notification!!.title.toString()
            notification["body"] = remoteMessage.notification!!.body.toString()
        }
        Log.e("fcm_message", "notification -> " + Gson().toJson(notification))
        Log.e("fcm_message", "data -> " + Gson().toJson(remoteMessage.data))

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            EkoStreamNotificationCreator.tryShowNotification(SampleApp.get(), remoteMessage)
        }
    }

    override fun onNewToken(token: String) {
        Log.e("fcm_new_token", token)
        EkoFcm.create()
                .setup(token)
                .subscribe()
    }
}
