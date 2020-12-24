package com.ekoapp.sdk.channellist.filter

interface ChannelQueryFilterContract {

    interface View {
        fun onSaveCompleted()
    }

    interface Presenter {
        fun saveFilterOption()
    }

}