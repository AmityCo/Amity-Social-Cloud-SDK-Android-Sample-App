package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.comment.ReplyCommentListActivity;

public class OpenReplyCommentListIntent extends SampleIntent {

    private static final String EXTRA_COMMENT_ID = EXTRA + "comment.id";
    private static final String EXTRA_POST_ID = EXTRA + "post.id";

    public OpenReplyCommentListIntent(@NonNull Context context, @NonNull String postId, String commentId) {
        super(context, ReplyCommentListActivity.class);
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
