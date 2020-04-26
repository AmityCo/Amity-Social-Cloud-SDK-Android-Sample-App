package com.ekoapp.sample.intent;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ekoapp.sample.usermetadata.ChangeMetadataActivity;

public class OpenChangeMetadataIntent extends BaseIntent {

    public OpenChangeMetadataIntent(@NonNull Context context) {
        super(context, ChangeMetadataActivity.class);
    }

}