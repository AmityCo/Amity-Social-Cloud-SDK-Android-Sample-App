package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.community.CommunitySingleMembershipActivity;

public class ViewCommunityMembershipIntent extends SampleIntent {

    private static final String EXTRA_COMMUNITY_ID = EXTRA + "community.id";
    private static final String EXTRA_USER_ID = EXTRA + "user.id";


    public ViewCommunityMembershipIntent(@NonNull Context context, @NonNull String communityId, @NonNull String userId) {
        super(context, CommunitySingleMembershipActivity.class);
        putExtra(EXTRA_COMMUNITY_ID, communityId);
        putExtra(EXTRA_USER_ID, userId);
    }


    public static String getCommunityId(Intent intent) {
        return intent.getStringExtra(EXTRA_COMMUNITY_ID);
    }

    public static String getUserId(Intent intent) {
        return intent.getStringExtra(EXTRA_USER_ID);
    }
}
