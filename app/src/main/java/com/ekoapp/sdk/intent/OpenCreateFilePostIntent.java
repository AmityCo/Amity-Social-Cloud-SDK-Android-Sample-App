package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.post.create.CreateFilePostActivity;

public class OpenCreateFilePostIntent extends SampleIntent {

    private static final String EXTRA_TARGET_TYPE = EXTRA + "target.type";
    private static final String EXTRA_TARGET_ID = EXTRA + "target.id";


    public OpenCreateFilePostIntent(@NonNull Context context, @NonNull String targetType, @NonNull String targetId) {
        super(context, CreateFilePostActivity.class);
        putExtra(EXTRA_TARGET_TYPE, targetType);
        putExtra(EXTRA_TARGET_ID, targetId);
    }

    public static String getTargetType(Intent intent) {
        return intent.getStringExtra(EXTRA_TARGET_TYPE);
    }

    public static String getTargetId(Intent intent) {
        return intent.getStringExtra(EXTRA_TARGET_ID);
    }
}