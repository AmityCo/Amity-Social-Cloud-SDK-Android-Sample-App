package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.comment.create.CreateCommentActivity;

public class OpenCreateCommentIntent extends SampleIntent {

    private static final String EXTRA_POST_ID = EXTRA + "post.id";

    public OpenCreateCommentIntent(@NonNull Context context, @NonNull String postId) {
        super(context, CreateCommentActivity.class);
        putExtra(EXTRA_POST_ID, postId);
    }

    public static String getPostId(Intent intent) {
        return intent.getStringExtra(EXTRA_POST_ID);
    }

}
