package com.ekoapp.simplechat.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.ekosdk.message.EkoMessage;
import com.ekoapp.simplechat.messagelist.ChildMessageListActivity;

public class ViewChildMessagesIntent extends BaseIntent {

    private static final String EXTRA_MESSAGE = EXTRA + "message";
    private static final String EXTRA_MESSAGE_DATA = EXTRA + "message.data";

    public ViewChildMessagesIntent(@NonNull Context context, @NonNull EkoMessage message, @NonNull EkoMessage.Data data) {
        super(context, ChildMessageListActivity.class);
        putExtra(EXTRA_MESSAGE, message);
        putExtra(EXTRA_MESSAGE_DATA, data);
    }

    public ViewChildMessagesIntent(@NonNull Context context, @NonNull EkoMessage message, @NonNull EkoMessage.Data.TEXT data) {
        super(context, ChildMessageListActivity.class);
        putExtra(EXTRA_MESSAGE, message);
        putExtra(EXTRA_MESSAGE_DATA, data);
    }

    public ViewChildMessagesIntent(@NonNull Context context, @NonNull EkoMessage message, @NonNull EkoMessage.Data.IMAGE data) {
        super(context, ChildMessageListActivity.class);
        putExtra(EXTRA_MESSAGE, message);
        putExtra(EXTRA_MESSAGE_DATA, data);
    }

    public ViewChildMessagesIntent(@NonNull Context context, @NonNull EkoMessage message, @NonNull EkoMessage.Data.FILE data) {
        super(context, ChildMessageListActivity.class);
        putExtra(EXTRA_MESSAGE, message);
        putExtra(EXTRA_MESSAGE_DATA, data);
    }

    public ViewChildMessagesIntent(@NonNull Context context, @NonNull EkoMessage message, @NonNull EkoMessage.Data.CUSTOM data) {
        super(context, ChildMessageListActivity.class);
        putExtra(EXTRA_MESSAGE, message);
        putExtra(EXTRA_MESSAGE_DATA, data);
    }

    public static EkoMessage getMessage(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE);
    }

    public static EkoMessage.Data getMessageData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE_DATA);
    }

    public static EkoMessage.Data.TEXT getTextMessageData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE_DATA);
    }

    public static EkoMessage.Data.IMAGE getImageMessageData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE_DATA);
    }


    public static EkoMessage.Data.FILE getFileMessageData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE_DATA);
    }

    public static EkoMessage.Data.CUSTOM getCustomMessageData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE_DATA);
    }
}
