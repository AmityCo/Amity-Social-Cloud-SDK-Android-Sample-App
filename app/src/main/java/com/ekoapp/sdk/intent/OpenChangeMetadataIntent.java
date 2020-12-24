package com.ekoapp.sdk.intent;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ekoapp.sdk.common.activity.ChangeMetadataActivity;


public class OpenChangeMetadataIntent extends SampleIntent {

    public OpenChangeMetadataIntent(@NonNull Context context) {
        super(context, ChangeMetadataActivity.class);
    }

}