package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.sample.ascsdk.userlist.MyUserFollowerListActivity;

public class OpenMyUserFollowerListIntent extends SampleIntent {

    private static final String EXTRA_FOLLOW_STATUS = EXTRA + "follow.status";

    public OpenMyUserFollowerListIntent(@NonNull Context context, String followStatus) {
        super(context, MyUserFollowerListActivity.class);
        putExtra(EXTRA_FOLLOW_STATUS, followStatus);
    }

    public static String getFollowStatus(Intent intent) {
        return intent.getStringExtra(EXTRA_FOLLOW_STATUS);
    }
}
