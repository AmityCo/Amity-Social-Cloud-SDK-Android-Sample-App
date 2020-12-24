package com.ekoapp.sdk.notificationsettings

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.ekoapp.sdk.R
import com.ekoapp.sdk.intent.StreamVideoPlayerIntent
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONArray

class EkoStreamNotificationCreator {

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun tryShowNotification(context: Context, remoteMessage: RemoteMessage) {
            createChannel(context)
            createNotification(context, remoteMessage)
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun createChannel(context: Context) {
            val manager = context.getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(EKO_SAMPLE_APP_CHANNEL_ID, "Simple notification", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun createNotification(context: Context, remoteMessage: RemoteMessage) {
            val streamId = parseStreamId(remoteMessage)
            val notificationBuilder = NotificationCompat.Builder(context, EKO_SAMPLE_APP_CHANNEL_ID)
                    .setSmallIcon(R.drawable.exo_icon_play)
                    .setContentTitle(remoteMessage.notification?.title ?: "")
                    .setContentText(remoteMessage.notification?.body ?: "")
                    .setOnlyAlertOnce(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setOngoing(false)
            streamId?.let { notificationBuilder.setContentIntent(pendingIntent(context, it)) }
            NotificationManagerCompat.from(context).notify(EKO_SAMPLE_APP_NOTIFICATION_TAG, System.currentTimeMillis().hashCode(), notificationBuilder.build())
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun pendingIntent(context: Context, streamId: String): PendingIntent? {
            val streamIntent = StreamVideoPlayerIntent(context, streamId)
            return TaskStackBuilder.create(context)
                    .also { builder -> builder.addNextIntent(streamIntent) }
                    .getPendingIntent(EKO_SAMPLE_APP_NOTIFICATION_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        private fun parseStreamId(remoteMessage: RemoteMessage): String? {
            remoteMessage.data[EKO_NOTIFICATION_VIDEO_STREAMING_KEY]?.let { jsonString ->
                val streamingJsonArray = JSONArray(jsonString)
                if (streamingJsonArray.length() > 0) {
                    return streamingJsonArray.getJSONObject(0).getString(EKO_NOTIFICATION_STREAM_ID_KEY)
                }
            }
            return null
        }
    }
}

private const val EKO_SAMPLE_APP_CHANNEL_ID = "EKO_SAMPLE_APP_CHANNEL_ID"
private const val EKO_SAMPLE_APP_NOTIFICATION_TAG = "EKO_SAMPLE_APP_NOTIFICATION_TAG"
private const val EKO_SAMPLE_APP_NOTIFICATION_REQUEST_CODE = 10210
private const val EKO_NOTIFICATION_VIDEO_STREAMING_KEY = "videoStreamings"
private const val EKO_NOTIFICATION_STREAM_ID_KEY = "streamId"