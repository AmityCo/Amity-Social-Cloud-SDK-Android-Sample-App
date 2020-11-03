package com.ekoapp.simplechat.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.ekosdk.comment.EkoComment;
import com.ekoapp.simplechat.comment.CommentReactionListActivity;

public class OpenCommentReactionListIntent extends BaseIntent {

    private static final String EXTRA_COMMENT = EXTRA + "comment";

    public OpenCommentReactionListIntent(@NonNull Context context, @NonNull EkoComment comment) {
        super(context, CommentReactionListActivity.class);
        putExtra(EXTRA_COMMENT, comment);
    }

    public static EkoComment getComment(Intent intent) {
        return intent.getParcelableExtra(EXTRA_COMMENT);
    }
}
