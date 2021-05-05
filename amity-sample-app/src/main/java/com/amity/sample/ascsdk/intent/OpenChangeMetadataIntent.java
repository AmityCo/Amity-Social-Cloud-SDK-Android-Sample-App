package com.amity.sample.ascsdk.intent;

import android.content.Context;

import androidx.annotation.NonNull;

import com.amity.sample.ascsdk.common.activity.ChangeMetadataActivity;


public class OpenChangeMetadataIntent extends SampleIntent {

    public OpenChangeMetadataIntent(@NonNull Context context) {
        super(context, ChangeMetadataActivity.class);
    }

}