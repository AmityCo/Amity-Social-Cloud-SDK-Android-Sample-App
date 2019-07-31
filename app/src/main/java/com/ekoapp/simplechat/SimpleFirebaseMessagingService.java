package com.ekoapp.simplechat;

import android.util.Log;

import com.ekoapp.push.EkoFcm;
import com.google.common.collect.Maps;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

public class SimpleFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("fcm_message", "messageType -> " + remoteMessage.getMessageType());
        Log.e("fcm_message", "priority -> " + remoteMessage.getPriority());

        Map<String, String> notification = Maps.newConcurrentMap();

        if (remoteMessage.getNotification() != null) {
            notification.put("title", String.valueOf(remoteMessage.getNotification().getTitle()));
            notification.put("body", String.valueOf(remoteMessage.getNotification().getBody()));
        }

        Log.e("fcm_message", "notification -> " + new Gson().toJson(notification));
        Log.e("fcm_message", "data -> " + new Gson().toJson(remoteMessage.getData()));
    }

    @Override
    public void onNewToken(String token) {
        Log.e("fcm_new_token", token);
        EkoFcm.create()
                .setup(token)
                .subscribe(() -> {

                }, throwable -> {

                });
    }
}
