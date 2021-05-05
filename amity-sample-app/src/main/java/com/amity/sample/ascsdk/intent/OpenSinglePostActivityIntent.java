package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.socialcloud.sdk.social.feed.AmityPost;
import com.amity.sample.ascsdk.post.SinglePostActivity;

public class OpenSinglePostActivityIntent extends SampleIntent {

    private static final String EXTRA_SINGLE_POST = EXTRA + "singlePost";

    public OpenSinglePostActivityIntent(@NonNull Context context, @NonNull AmityPost post) {
        super(context, SinglePostActivity.class);
        putExtra(EXTRA_SINGLE_POST, post);
    }

    public static AmityPost getPost(Intent intent) {
        return intent.getParcelableExtra(EXTRA_SINGLE_POST);
    }

}
