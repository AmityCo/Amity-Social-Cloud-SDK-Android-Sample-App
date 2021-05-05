package com.amity.sample.ascsdk.stream

import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.upstream.DataSource

class StreamVideoPlayerRMTPActivity : StreamAmityVideoPlayerActivity() {

    fun getDataSourceFactory(): DataSource.Factory = RtmpDataSourceFactory()

}