package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.ekosdk.community.EkoCommunity;
import com.ekoapp.sdk.post.CommunityFeedActivity;

public class OpenCommunityFeedIntent extends SampleIntent {

    private static final String EXTRA_COMMUNITY = EXTRA + "community";

    public OpenCommunityFeedIntent(@NonNull Context context, @NonNull EkoCommunity community) {
        super(context, CommunityFeedActivity.class);
        putExtra(EXTRA_COMMUNITY, community);
    }

    public static EkoCommunity getCommunity(Intent intent) {
        return intent.getParcelableExtra(EXTRA_COMMUNITY);
    }

}
