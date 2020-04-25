package com.ekoapp.sample.chatfeature.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sample.chatfeature.messagelist.ParentMessageListActivity;

public class ViewParentMessagesIntent extends BaseIntent {

    private static final String EXTRA_CHANNEL_ID = EXTRA + "channel.id";


    public ViewParentMessagesIntent(@NonNull Context context, @NonNull String channelId) {
        super(context, ParentMessageListActivity.class);
        putExtra(EXTRA_CHANNEL_ID, channelId);
    }


    public static String getChannelId(Intent intent) {
        return intent.getStringExtra(EXTRA_CHANNEL_ID);
    }
}
