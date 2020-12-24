package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.messagelist.FileMessageSenderActivity;


public class OpenFileMessageSenderIntent extends SampleIntent {

    private static final String EXTRA_CHANNEL_ID = EXTRA + "channel.id";
    private static final String EXTRA_PARENT_ID = EXTRA + "parent.id";

    public OpenFileMessageSenderIntent(@NonNull Context context, @NonNull String channelId) {
        super(context, FileMessageSenderActivity.class);
        putExtra(EXTRA_CHANNEL_ID, channelId);
    }

    public OpenFileMessageSenderIntent(@NonNull Context context, @NonNull String channelId, @NonNull String parentId) {
        super(context, FileMessageSenderActivity.class);
        putExtra(EXTRA_CHANNEL_ID, channelId);
        putExtra(EXTRA_PARENT_ID, parentId);
    }

    public static String getChannelId(Intent intent) {
        return intent.getStringExtra(EXTRA_CHANNEL_ID);
    }

    public static String getParentId(Intent intent) {
        return intent.getStringExtra(EXTRA_PARENT_ID);
    }
}
