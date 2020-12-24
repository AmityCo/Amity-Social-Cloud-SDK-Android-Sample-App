package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.ekosdk.feed.EkoPost;
import com.ekoapp.sdk.post.reaction.PostReactionActivity;

public class OpenPostReactionActivityIntent extends SampleIntent {

    private static final String EXTRA_POST = EXTRA + "post";

    public OpenPostReactionActivityIntent(@NonNull Context context, @NonNull EkoPost post) {
        super(context, PostReactionActivity.class);
        putExtra(EXTRA_POST, post);
    }

    public static EkoPost getPost(Intent intent) {
        return intent.getParcelableExtra(EXTRA_POST);
    }

}
