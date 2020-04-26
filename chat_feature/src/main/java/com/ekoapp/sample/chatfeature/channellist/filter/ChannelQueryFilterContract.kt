package com.ekoapp.sample.chatfeature.channellist.filter

interface ChannelQueryFilterContract {

    interface View {
        fun onSaveCompleted()
    }

    interface Presenter {
        fun saveFilterOption()
    }

}