package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.sample.ascsdk.post.CommunityPostPagingActivity;
import com.amity.socialcloud.sdk.social.community.AmityCommunity;

public class OpenCommunityPostPagingIntent extends SampleIntent {

    private static final String EXTRA_COMMUNITY = EXTRA + "community";

    public OpenCommunityPostPagingIntent(@NonNull Context context, @NonNull AmityCommunity community) {
        super(context, CommunityPostPagingActivity.class);
        putExtra(EXTRA_COMMUNITY, community);
    }

    public static AmityCommunity getCommunity(Intent intent) {
        return intent.getParcelableExtra(EXTRA_COMMUNITY);
    }

}
