package com.ekoapp.sdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.BuildConfig;

abstract class SampleIntent extends Intent {

    public static final String ACTION = BuildConfig.APPLICATION_ID + ".intent.action.";
    public static final String EXTRA = BuildConfig.APPLICATION_ID + ".intent.extra.";


    protected SampleIntent(String action) {
        super(action);
    }

    protected SampleIntent(@NonNull Context context, @NonNull Class<?> cls) {
        super(context, cls);
    }
}
