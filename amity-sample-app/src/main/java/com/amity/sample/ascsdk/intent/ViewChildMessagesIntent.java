package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.socialcloud.sdk.chat.message.AmityMessage;
import com.amity.sample.ascsdk.messagelist.ChildMessageListActivity;

public class ViewChildMessagesIntent extends SampleIntent {

    private static final String EXTRA_MESSAGE = EXTRA + "message";
    private static final String EXTRA_MESSAGE_DATA = EXTRA + "message.data";

    public ViewChildMessagesIntent(@NonNull Context context, @NonNull AmityMessage message, @NonNull AmityMessage.Data data) {
        super(context, ChildMessageListActivity.class);
        putExtra(EXTRA_MESSAGE, message);
        putExtra(EXTRA_MESSAGE_DATA, data);
    }

    public ViewChildMessagesIntent(@NonNull Context context, @NonNull AmityMessage message, @NonNull AmityMessage.Data.TEXT data) {
        super(context, ChildMessageListActivity.class);
        putExtra(EXTRA_MESSAGE, message);
        putExtra(EXTRA_MESSAGE_DATA, data);
    }

    public ViewChildMessagesIntent(@NonNull Context context, @NonNull AmityMessage message, @NonNull AmityMessage.Data.IMAGE data) {
        super(context, ChildMessageListActivity.class);
        putExtra(EXTRA_MESSAGE, message);
        putExtra(EXTRA_MESSAGE_DATA, data);
    }

    public ViewChildMessagesIntent(@NonNull Context context, @NonNull AmityMessage message, @NonNull AmityMessage.Data.FILE data) {
        super(context, ChildMessageListActivity.class);
        putExtra(EXTRA_MESSAGE, message);
        putExtra(EXTRA_MESSAGE_DATA, data);
    }

    public ViewChildMessagesIntent(@NonNull Context context, @NonNull AmityMessage message, @NonNull AmityMessage.Data.CUSTOM data) {
        super(context, ChildMessageListActivity.class);
        putExtra(EXTRA_MESSAGE, message);
        putExtra(EXTRA_MESSAGE_DATA, data);
    }

    public static AmityMessage getMessage(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE);
    }

    public static AmityMessage.Data getMessageData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE_DATA);
    }

    public static AmityMessage.Data.TEXT getTextMessageData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE_DATA);
    }

    public static AmityMessage.Data.IMAGE getImageMessageData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE_DATA);
    }


    public static AmityMessage.Data.FILE getFileMessageData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE_DATA);
    }

    public static AmityMessage.Data.CUSTOM getCustomMessageData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MESSAGE_DATA);
    }

}
