package com.ekoapp.simplechat.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.simplechat.TextMessageEditorActivity;

public class OpenTextMessageEditorActivityIntent extends BaseIntent {

    public static final String EXTRA_TEXT = EXTRA + "text";

    public OpenTextMessageEditorActivityIntent(@NonNull Context context, @NonNull String text) {
        super(context, TextMessageEditorActivity.class);
        putExtra(EXTRA_TEXT, text);
    }

    public static String getText(Intent intent) {
        return intent.getStringExtra(EXTRA_TEXT);
    }
}
