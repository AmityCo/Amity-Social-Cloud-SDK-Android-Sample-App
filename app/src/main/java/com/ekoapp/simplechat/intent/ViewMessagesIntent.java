package com.ekoapp.simplechat.intent;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.ekoapp.simplechat.MessageListActivity;

public class ViewMessagesIntent extends BaseIntent {

    private static final String EXTRA_CHANNEL_ID = EXTRA + "channel.id";


    public ViewMessagesIntent(@NonNull Context context, @NonNull String channelId) {
        super(context, MessageListActivity.class);
        putExtra(EXTRA_CHANNEL_ID, channelId);
    }


    public static String getChannelId(Intent intent) {
        return intent.getStringExtra(EXTRA_CHANNEL_ID);
    }
}
