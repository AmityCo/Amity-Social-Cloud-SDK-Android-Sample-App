package com.ekoapp.simplechat.stream

import com.ekoapp.sdk.stream.LiveStreamListActivity

class RecordedStreamListActivity : LiveStreamListActivity() {

    override fun isLive(): Boolean = false

    override fun subtitle(): String = "Recorded Stream"

}