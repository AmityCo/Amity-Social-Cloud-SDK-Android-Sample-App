package com.ekoapp.simplechat.intent;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ekoapp.simplechat.channellist.ChangeMetadataActivity;

public class OpenChangeMetadataIntent extends BaseIntent {

    public OpenChangeMetadataIntent(@NonNull Context context) {
        super(context, ChangeMetadataActivity.class);
    }

}