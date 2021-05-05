package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.socialcloud.sdk.chat.channel.AmityChannel;
import com.amity.sample.ascsdk.messagelist.ParentMessageListActivity;


public class ViewParentMessagesIntent extends SampleIntent {

    private static final String EXTRA_CHANNEL = EXTRA + "channel";


    public ViewParentMessagesIntent(@NonNull Context context, @NonNull AmityChannel channel) {
        super(context, ParentMessageListActivity.class);
        putExtra(EXTRA_CHANNEL, channel);
    }


    public static AmityChannel getChannel(Intent intent) {

        return intent.getParcelableExtra(EXTRA_CHANNEL);
    }
}
