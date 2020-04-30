package com.ekoapp.sample.chatfeature.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sample.chatfeature.messagelist.ChildMessageListActivity;
import com.ekoapp.sample.core.intent.BaseIntent;

public class ViewChildMessagesIntent extends BaseIntent {

    private static final String EXTRA_CHANNEL_ID = EXTRA + "channel.id";
    private static final String EXTRA_PARENT_ID = EXTRA + "parent.id";
    private static final String EXTRA_DATA = EXTRA + "data";


    public ViewChildMessagesIntent(@NonNull Context context, @NonNull String channelId, @NonNull String parentId, @NonNull String data) {
        super(context, ChildMessageListActivity.class);
        putExtra(EXTRA_CHANNEL_ID, channelId);
        putExtra(EXTRA_PARENT_ID, parentId);
        putExtra(EXTRA_DATA, data);
    }


    public static String getChannelId(Intent intent) {
        return intent.getStringExtra(EXTRA_CHANNEL_ID);
    }

    public static String getParentId(Intent intent) {
        return intent.getStringExtra(EXTRA_PARENT_ID);
    }

    public static String getData(Intent intent) {
        return intent.getStringExtra(EXTRA_DATA);
    }
}
