package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.post.create.CreateImagePostActivity;

public class OpenCreateImagePostIntent extends SampleIntent {

    private static final String EXTRA_TARGET_TYPE = EXTRA + "target.type";
    private static final String EXTRA_TARGET_ID = EXTRA + "target.id";


    public OpenCreateImagePostIntent(@NonNull Context context, @NonNull String targetType, @NonNull String targetId) {
        super(context, CreateImagePostActivity.class);
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