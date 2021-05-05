package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.socialcloud.sdk.social.feed.AmityPost;
import com.amity.sample.ascsdk.post.reaction.PostReactionActivity;

public class OpenPostReactionActivityIntent extends SampleIntent {

    private static final String EXTRA_POST = EXTRA + "post";

    public OpenPostReactionActivityIntent(@NonNull Context context, @NonNull AmityPost post) {
        super(context, PostReactionActivity.class);
        putExtra(EXTRA_POST, post);
    }

    public static AmityPost getPost(Intent intent) {
        return intent.getParcelableExtra(EXTRA_POST);
    }

}
