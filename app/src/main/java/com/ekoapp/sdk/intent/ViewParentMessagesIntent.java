package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.ekosdk.channel.EkoChannel;
import com.ekoapp.sdk.messagelist.ParentMessageListActivity;


public class ViewParentMessagesIntent extends SampleIntent {

    private static final String EXTRA_CHANNEL = EXTRA + "channel";


    public ViewParentMessagesIntent(@NonNull Context context, @NonNull EkoChannel channel) {
        super(context, ParentMessageListActivity.class);
        putExtra(EXTRA_CHANNEL, channel);
    }


    public static EkoChannel getChannel(Intent intent) {

        return intent.getParcelableExtra(EXTRA_CHANNEL);
    }
}
