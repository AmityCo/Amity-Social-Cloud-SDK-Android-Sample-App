package com.ekoapp.simplechat.intent;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.ekoapp.simplechat.BuildConfig;

public class BaseIntent extends Intent {

    protected static final String ACTION = BuildConfig.APPLICATION_ID + ".intent.action.";
    protected static final String EXTRA = BuildConfig.APPLICATION_ID + ".intent.extra.";


    protected BaseIntent(String action) {
        super(action);
    }

    protected BaseIntent(@NonNull Context context, @NonNull Class<?> cls) {
        super(context, cls);
    }
}
