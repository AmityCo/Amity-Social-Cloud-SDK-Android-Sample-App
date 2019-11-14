package com.ekoapp.simplechat.intent;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ekoapp.simplechat.CustomMessageEditorActivity;

public class OpenCustomMessageEditorActivityIntent extends BaseIntent {

    public static final String EXTRA_MAP_KEY = EXTRA + "map.key";
    public static final String EXTRA_MAP_VALUE = EXTRA + "map.value";

    public OpenCustomMessageEditorActivityIntent(@NonNull Context context) {
        super(context, CustomMessageEditorActivity.class);
    }
}
