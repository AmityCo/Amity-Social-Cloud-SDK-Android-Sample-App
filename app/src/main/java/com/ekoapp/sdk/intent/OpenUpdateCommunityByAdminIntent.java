package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.community.update.UpdateCommunityByAdminActivity;


public class OpenUpdateCommunityByAdminIntent extends SampleIntent {

    private static final String EXTRA_COMMUNITY_ID = EXTRA + "community.id";

    public OpenUpdateCommunityByAdminIntent(@NonNull Context context, @NonNull String communityId) {
        super(context, UpdateCommunityByAdminActivity.class);
        putExtra(EXTRA_COMMUNITY_ID, communityId);
    }

    public static String getCommunityId(Intent intent) {
        return intent.getStringExtra(EXTRA_COMMUNITY_ID);
    }

}
