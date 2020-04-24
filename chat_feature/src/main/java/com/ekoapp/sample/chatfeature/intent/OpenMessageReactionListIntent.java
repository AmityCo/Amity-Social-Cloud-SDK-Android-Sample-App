package com.ekoapp.sample.chatfeature.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sample.chatfeature.messagereactionlist.MessageReactionListActivity;
import com.ekoapp.sample.intent.BaseIntent;

public class OpenMessageReactionListIntent extends BaseIntent {

    private static final String EXTRA_MESSAGE_ID = EXTRA + "message.id";

    public OpenMessageReactionListIntent(@NonNull Context context, @NonNull String messageId) {
        super(context, MessageReactionListActivity.class);
        putExtra(EXTRA_MESSAGE_ID, messageId);
    }

    public static String getMessageId(Intent intent) {
        return intent.getStringExtra(EXTRA_MESSAGE_ID);
    }
}
