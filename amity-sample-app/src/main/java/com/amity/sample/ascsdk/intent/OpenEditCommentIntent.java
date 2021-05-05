package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.socialcloud.sdk.social.comment.AmityComment;
import com.amity.sample.ascsdk.comment.update.EditCommentActivity;

public class OpenEditCommentIntent extends SampleIntent {

    private static final String EXTRA_COMMENT = EXTRA + "comment";

    public OpenEditCommentIntent(@NonNull Context context, @NonNull AmityComment comment) {
        super(context, EditCommentActivity.class);
        putExtra(EXTRA_COMMENT, comment);
    }

    public static AmityComment getComment(Intent intent) {
        return intent.getExtras().getParcelable(EXTRA_COMMENT);
    }

}
