package com.ekoapp.simplechat.channellist.filter

interface ChannelQueryFilterContract {

    interface View {
        fun onSaveCompleted()
    }

    interface Presenter {
        fun saveFilterOption()
    }

}