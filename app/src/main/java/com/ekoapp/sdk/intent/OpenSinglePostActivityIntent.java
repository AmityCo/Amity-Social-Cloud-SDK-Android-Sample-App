package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.ekosdk.feed.EkoPost;
import com.ekoapp.sdk.post.SinglePostActivity;

public class OpenSinglePostActivityIntent extends SampleIntent {

    private static final String EXTRA_SINGLE_POST = EXTRA + "singlePost";

    public OpenSinglePostActivityIntent(@NonNull Context context, @NonNull EkoPost post) {
        super(context, SinglePostActivity.class);
        putExtra(EXTRA_SINGLE_POST, post);
    }

    public static EkoPost getPost(Intent intent) {
        return intent.getParcelableExtra(EXTRA_SINGLE_POST);
    }

}
