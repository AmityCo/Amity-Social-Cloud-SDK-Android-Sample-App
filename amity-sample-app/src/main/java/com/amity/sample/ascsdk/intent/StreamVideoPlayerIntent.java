package com.amity.sample.ascsdk.intent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amity.socialcloud.sdk.video.stream.AmityStream;
import com.amity.sample.ascsdk.stream.StreamAmityVideoPlayerActivity;


public class StreamVideoPlayerIntent extends SampleIntent {

    private static final String EXTRA_STREAM = EXTRA + "stream";
    private static final String EXTRA_STREAM_ID = EXTRA + "stream_id";

    public StreamVideoPlayerIntent(@NonNull Context context, @NonNull AmityStream stream) {
        super(context, StreamAmityVideoPlayerActivity.class);
        putExtra(EXTRA_STREAM, stream);
    }

    public StreamVideoPlayerIntent(@NonNull Context context, @NonNull String streamId) {
        super(context, StreamAmityVideoPlayerActivity.class);
        putExtra(EXTRA_STREAM_ID, streamId);
    }

    public StreamVideoPlayerIntent(@NonNull Context context, @NonNull String streamId, boolean showHomePage) {
        super(context, StreamAmityVideoPlayerActivity.class);
        putExtra(EXTRA_STREAM_ID, streamId);
    }

    public static AmityStream getStream(Intent intent) {
        return intent.getParcelableExtra(EXTRA_STREAM);
    }

    public static String getStreamId(Intent intent) {
        return intent.getStringExtra(EXTRA_STREAM_ID);
    }
}
