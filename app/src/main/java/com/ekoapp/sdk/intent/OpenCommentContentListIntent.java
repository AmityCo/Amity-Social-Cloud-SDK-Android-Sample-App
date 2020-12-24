package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.comment.CommentContentListActivity;

public class OpenCommentContentListIntent extends SampleIntent {

    private static final String EXTRA_CONTENT_ID = EXTRA + "content.id";

    public OpenCommentContentListIntent(@NonNull Context context, @NonNull String contentId) {
        super(context, CommentContentListActivity.class);
        putExtra(EXTRA_CONTENT_ID, contentId);
    }

    public static String getContentId(Intent intent) {
        return intent.getStringExtra(EXTRA_CONTENT_ID);
    }

}
