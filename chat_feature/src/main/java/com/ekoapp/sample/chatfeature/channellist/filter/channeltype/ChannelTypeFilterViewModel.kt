package com.ekoapp.sample.chatfeature.channellist.filter.channeltype

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChannelTypeFilterViewModel : ViewModel() {

    var isStandardTypeSelected: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        this.value = false
    }

    var isPrivateTypeSelected: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        this.value = false
    }

    var isChatTypeSelected: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        this.value = false
    }

    var isBroadcastTypeSelected: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        this.value = false
    }

}