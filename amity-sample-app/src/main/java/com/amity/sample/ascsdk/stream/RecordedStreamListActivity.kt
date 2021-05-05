package com.amity.sample.ascsdk.stream

import com.amity.socialcloud.sdk.video.stream.AmityStream

class RecordedStreamListActivity : LiveStreamListActivity() {

    override fun getStatus(): Array<AmityStream.Status> = arrayOf(AmityStream.Status.RECORDED)
    override fun subtitle(): String = "Recorded Stream"

}