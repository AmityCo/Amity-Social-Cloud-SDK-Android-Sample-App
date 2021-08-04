package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.sample.ascsdk.post.UserPostPagingActivity;

public class OpenUserPostPagingIntent extends SampleIntent {

    private static final String EXTRA_USER_ID = EXTRA + "user.id";

    public OpenUserPostPagingIntent(@NonNull Context context, @NonNull String channelId) {
        super(context, UserPostPagingActivity.class);
        putExtra(EXTRA_USER_ID, channelId);
    }

    public static String getUserId(Intent intent) {
        return intent.getStringExtra(EXTRA_USER_ID);
    }

}
