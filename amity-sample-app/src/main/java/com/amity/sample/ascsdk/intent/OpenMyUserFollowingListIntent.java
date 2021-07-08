package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.sample.ascsdk.userlist.MyUserFollowingListActivity;

public class OpenMyUserFollowingListIntent extends SampleIntent {

    private static final String EXTRA_FOLLOW_STATUS = EXTRA + "follow.status";

    public OpenMyUserFollowingListIntent(@NonNull Context context, String followStatus) {
        super(context, MyUserFollowingListActivity.class);
        putExtra(EXTRA_FOLLOW_STATUS, followStatus);
    }

    public static String getFollowStatus(Intent intent) {
        return intent.getStringExtra(EXTRA_FOLLOW_STATUS);
    }
}
