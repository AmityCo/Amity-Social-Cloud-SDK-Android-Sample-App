package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.socialcloud.sdk.social.comment.AmityComment;
import com.amity.sample.ascsdk.reaction.CommentReactionListActivity;

public class OpenCommentReactionListIntent extends SampleIntent {

    private static final String EXTRA_COMMENT = EXTRA + "comment";

    public OpenCommentReactionListIntent(@NonNull Context context, @NonNull AmityComment comment) {
        super(context, CommentReactionListActivity.class);
        putExtra(EXTRA_COMMENT, comment);
    }

    public static AmityComment getComment(Intent intent) {
        return intent.getParcelableExtra(EXTRA_COMMENT);
    }
}
