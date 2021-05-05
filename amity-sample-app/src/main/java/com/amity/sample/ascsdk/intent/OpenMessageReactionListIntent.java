package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.socialcloud.sdk.chat.message.AmityMessage;
import com.amity.sample.ascsdk.reaction.MessageReactionListActivity;

public class OpenMessageReactionListIntent extends SampleIntent {

    private static final String EXTRA_PARCEL_MESSAGE = EXTRA + "message";

    public OpenMessageReactionListIntent(@NonNull Context context, @NonNull AmityMessage message) {
        super(context, MessageReactionListActivity.class);
        putExtra(EXTRA_PARCEL_MESSAGE, message);
    }

    public static AmityMessage getMessage(Intent intent) {
        return intent.getParcelableExtra(EXTRA_PARCEL_MESSAGE);
    }
}
