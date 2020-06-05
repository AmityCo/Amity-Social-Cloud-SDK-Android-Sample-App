package com.ekoapp.sample.chatfeature.usermetadata;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ekoapp.sample.core.intent.BaseIntent;


public class OpenChangeMetadataIntent extends BaseIntent {

    public OpenChangeMetadataIntent(@NonNull Context context) {
        super(context, ChangeMetadataActivity.class);
    }

}