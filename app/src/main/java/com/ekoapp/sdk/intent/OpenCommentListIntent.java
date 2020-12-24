package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.comment.CommentListActivity;

public class OpenCommentListIntent extends SampleIntent {

    private static final String EXTRA_POST_ID = EXTRA + "post.id";

    public OpenCommentListIntent(@NonNull Context context, @NonNull String postId) {
        super(context, CommentListActivity.class);
        putExtra(EXTRA_POST_ID, postId);
    }

    public static String getPostId(Intent intent) {
        return intent.getStringExtra(EXTRA_POST_ID);
    }

}
