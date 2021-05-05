package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.sample.ascsdk.messagelist.AudioMessageSenderActivity;

public class OpenAudioMessageSenderIntent extends SampleIntent {

    private static final String EXTRA_CHANNEL_ID = EXTRA + "channel.id";
    private static final String EXTRA_PARENT_ID = EXTRA + "parent.id";

    public OpenAudioMessageSenderIntent(@NonNull Context context, @NonNull String channelId) {
        super(context, AudioMessageSenderActivity.class);
        putExtra(EXTRA_CHANNEL_ID, channelId);
    }

    public OpenAudioMessageSenderIntent(@NonNull Context context, @NonNull String channelId, @NonNull String parentId) {
        super(context, AudioMessageSenderActivity.class);
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
