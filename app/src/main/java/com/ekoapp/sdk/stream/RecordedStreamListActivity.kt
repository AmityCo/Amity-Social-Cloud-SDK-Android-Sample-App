package com.ekoapp.sdk.stream

import com.ekoapp.ekosdk.stream.EkoStream

class RecordedStreamListActivity : LiveStreamListActivity() {

    override fun getStatus(): Array<EkoStream.Status> = arrayOf(EkoStream.Status.RECORDED)
    override fun subtitle(): String = "Recorded Stream"

}