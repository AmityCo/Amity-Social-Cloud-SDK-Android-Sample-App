package com.ekoapp.sample.chatfeature.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sample.chatfeature.messagelist.CustomMessageSenderActivity;
import com.ekoapp.sample.core.intent.BaseIntent;

public class OpenCustomMessageSenderIntent extends BaseIntent {

    private static final String EXTRA_CHANNEL_ID = EXTRA + "channel.id";
    private static final String EXTRA_PARENT_ID = EXTRA + "parent.id";

    public OpenCustomMessageSenderIntent(@NonNull Context context, @NonNull String channelId) {
        super(context, CustomMessageSenderActivity.class);
        putExtra(EXTRA_CHANNEL_ID, channelId);
    }

    public OpenCustomMessageSenderIntent(@NonNull Context context, @NonNull String channelId, @NonNull String parentId) {
        super(context, CustomMessageSenderActivity.class);
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