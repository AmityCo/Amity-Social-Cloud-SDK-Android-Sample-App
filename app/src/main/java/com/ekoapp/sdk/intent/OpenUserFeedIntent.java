package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.post.UserFeedActivity;

public class OpenUserFeedIntent extends SampleIntent {

    private static final String EXTRA_USER_ID = EXTRA + "user.id";

    public OpenUserFeedIntent(@NonNull Context context, @NonNull String channelId) {
        super(context, UserFeedActivity.class);
        putExtra(EXTRA_USER_ID, channelId);
    }

    public static String getUserId(Intent intent) {
        return intent.getStringExtra(EXTRA_USER_ID);
    }

}
