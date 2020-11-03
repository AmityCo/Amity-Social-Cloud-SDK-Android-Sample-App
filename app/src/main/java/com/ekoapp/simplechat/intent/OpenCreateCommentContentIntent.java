package com.ekoapp.simplechat.intent;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.ekoapp.simplechat.comment.CreateCommentContentActivity;

public class OpenCreateCommentContentIntent extends BaseIntent {

    private static final String EXTRA_CONTENT_ID = EXTRA + "content.id";

    public OpenCreateCommentContentIntent(@NonNull Context context, @NonNull String contentId) {
        super(context, CreateCommentContentActivity.class);
        putExtra(EXTRA_CONTENT_ID, contentId);
    }

    public static String getContentId(Intent intent) {
        return intent.getStringExtra(EXTRA_CONTENT_ID);
    }

}
