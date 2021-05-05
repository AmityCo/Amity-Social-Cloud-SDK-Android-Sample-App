package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.sample.ascsdk.community.update.UpdateCommunityActivity;

public class OpenUpdateCommunityIntent extends SampleIntent {

    private static final String EXTRA_COMMUNITY_ID = EXTRA + "community.id";

    public OpenUpdateCommunityIntent(@NonNull Context context, @NonNull String communityId) {
        super(context, UpdateCommunityActivity.class);
        putExtra(EXTRA_COMMUNITY_ID, communityId);
    }

    public static String getCommunityId(Intent intent) {
        return intent.getStringExtra(EXTRA_COMMUNITY_ID);
    }

}
