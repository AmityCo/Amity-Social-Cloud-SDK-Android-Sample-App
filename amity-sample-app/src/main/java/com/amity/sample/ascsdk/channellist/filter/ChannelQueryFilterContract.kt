package com.amity.sample.ascsdk.channellist.filter

interface ChannelQueryFilterContract {

    interface View {
        fun onSaveCompleted()
    }

    interface Presenter {
        fun saveFilterOption()
    }

}