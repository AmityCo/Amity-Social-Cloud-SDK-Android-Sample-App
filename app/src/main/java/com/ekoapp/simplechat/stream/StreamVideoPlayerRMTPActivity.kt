package com.ekoapp.simplechat.stream

import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.upstream.DataSource

class StreamVideoPlayerRMTPActivity : StreamVideoPlayerActivity() {

    override fun getDataSourceFactory(): DataSource.Factory = RtmpDataSourceFactory()

}