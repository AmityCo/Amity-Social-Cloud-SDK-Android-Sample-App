package com.ekoapp.simplechat.stream;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ekoapp.ekosdk.stream.EkoStream;
import com.ekoapp.simplechat.intent.BaseIntent;


public class StreamVideoPlayerIntent extends BaseIntent {

    private static final String EXTRA_STREAM = EXTRA + "stream";
    private static final String EXTRA_STREAM_ID = EXTRA + "stream_id";

    public StreamVideoPlayerIntent(@NonNull Context context, @NonNull EkoStream stream) {
        super(context, StreamVideoPlayerActivity.class);
        putExtra(EXTRA_STREAM, stream);
    }

    public StreamVideoPlayerIntent(@NonNull Context context, @NonNull String streamId) {
        super(context, StreamVideoPlayerActivity.class);
        putExtra(EXTRA_STREAM_ID, streamId);
    }

    public static EkoStream getStream(Intent intent) {
        return intent.getParcelableExtra(EXTRA_STREAM);
    }

    public static String getStreamId(Intent intent) {
        return intent.getStringExtra(EXTRA_STREAM_ID);
    }
}
