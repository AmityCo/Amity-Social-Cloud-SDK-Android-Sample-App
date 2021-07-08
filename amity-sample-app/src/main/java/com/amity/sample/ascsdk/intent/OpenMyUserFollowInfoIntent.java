package com.amity.sample.ascsdk.intent;

import android.content.Context;

import androidx.annotation.NonNull;

import com.amity.sample.ascsdk.userlist.MyUserFollowInfoActivity;

public class OpenMyUserFollowInfoIntent extends SampleIntent {

    public OpenMyUserFollowInfoIntent(@NonNull Context context) {
        super(context, MyUserFollowInfoActivity.class);
    }
}
