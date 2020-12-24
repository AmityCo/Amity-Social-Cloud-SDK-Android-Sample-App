package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.ekosdk.message.EkoMessage;
import com.ekoapp.sdk.reaction.MessageReactionListActivity;

public class OpenMessageReactionListIntent extends SampleIntent {

    private static final String EXTRA_PARCEL_MESSAGE = EXTRA + "message";

    public OpenMessageReactionListIntent(@NonNull Context context, @NonNull EkoMessage message) {
        super(context, MessageReactionListActivity.class);
        putExtra(EXTRA_PARCEL_MESSAGE, message);
    }

    public static EkoMessage getMessage(Intent intent) {
        return intent.getParcelableExtra(EXTRA_PARCEL_MESSAGE);
    }
}
