package com.ekoapp.sample.chatfeature.channels.filter

interface ChannelQueryFilterContract {

    interface View {
        fun onSaveCompleted()
    }

    interface Presenter {
        fun saveFilterOption()
    }

}