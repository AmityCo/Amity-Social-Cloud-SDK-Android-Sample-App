package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.sample.ascsdk.userlist.UserFollowerListActivity;

public class OpenUserFollowerListIntent extends SampleIntent {

    private static final String EXTRA_USER_ID = EXTRA + "user.id";

    public OpenUserFollowerListIntent(@NonNull Context context, @NonNull String userId) {
        super(context, UserFollowerListActivity.class);
        putExtra(EXTRA_USER_ID, userId);
    }

    public static String getUserId(Intent intent) {
        return intent.getStringExtra(EXTRA_USER_ID);
    }

}
