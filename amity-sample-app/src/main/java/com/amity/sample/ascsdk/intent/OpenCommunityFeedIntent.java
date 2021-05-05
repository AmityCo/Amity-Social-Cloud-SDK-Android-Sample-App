package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.socialcloud.sdk.social.community.AmityCommunity;
import com.amity.sample.ascsdk.post.CommunityFeedActivity;

public class OpenCommunityFeedIntent extends SampleIntent {

    private static final String EXTRA_COMMUNITY = EXTRA + "community";

    public OpenCommunityFeedIntent(@NonNull Context context, @NonNull AmityCommunity community) {
        super(context, CommunityFeedActivity.class);
        putExtra(EXTRA_COMMUNITY, community);
    }

    public static AmityCommunity getCommunity(Intent intent) {
        return intent.getParcelableExtra(EXTRA_COMMUNITY);
    }

}
