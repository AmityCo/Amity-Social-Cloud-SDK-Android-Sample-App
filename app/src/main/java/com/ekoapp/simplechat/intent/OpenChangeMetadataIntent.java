package com.ekoapp.simplechat.intent;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ekoapp.simplechat.ChangeMetadataActivity;

public class OpenChangeMetadataIntent extends BaseIntent {

    public OpenChangeMetadataIntent(@NonNull Context context) {
        super(context, ChangeMetadataActivity.class);
    }

}