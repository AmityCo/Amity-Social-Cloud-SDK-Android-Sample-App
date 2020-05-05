package com.ekoapp.sample.core.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;


public class BaseIntent extends Intent {

    protected static final String ACTION = "intent.action.";
    protected static final String EXTRA = "intent.extra.";


    protected BaseIntent(String action) {
        super(action);
    }

    protected BaseIntent(@NonNull Context context, @NonNull Class<?> cls) {
        super(context, cls);
    }
}
