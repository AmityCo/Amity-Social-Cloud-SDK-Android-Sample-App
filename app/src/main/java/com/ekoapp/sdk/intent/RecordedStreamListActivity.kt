package com.ekoapp.simplechat.stream

class RecordedStreamListActivity : LiveStreamListActivity() {

    override fun isLive(): Boolean = false

    override fun subtitle(): String = "Recorded Stream"

}