package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.sample.ascsdk.comment.create.CreateCommentActivity;

public class OpenCreateCommentIntent extends SampleIntent {

    private static final String EXTRA_POST_ID = EXTRA + "post.id";
    private static final String EXTRA_COMMENT_ID = EXTRA + "comment.id";

    public OpenCreateCommentIntent(@NonNull Context context, @NonNull String postId) {
        super(context, CreateCommentActivity.class);
        putExtra(EXTRA_POST_ID, postId);
    }

    public OpenCreateCommentIntent(@NonNull Context context, @NonNull String postId, @NonNull String commentId) {
        super(context, CreateCommentActivity.class);
        putExtra(EXTRA_POST_ID, postId);
        putExtra(EXTRA_COMMENT_ID, commentId);
    }

    public static String getPostId(Intent intent) {
        return intent.getStringExtra(EXTRA_POST_ID);
    }

    public static String getCommentId(Intent intent) {
        return intent.getStringExtra(EXTRA_COMMENT_ID);
    }
}
