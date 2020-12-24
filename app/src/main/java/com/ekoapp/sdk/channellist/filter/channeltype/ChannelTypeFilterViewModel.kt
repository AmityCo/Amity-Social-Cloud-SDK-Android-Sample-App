package com.ekoapp.sdk.channellist.filter.channeltype

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChannelTypeFilterViewModel : ViewModel() {

    var isCommunityTypeSelected: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        this.value = false
    }

    var isStandardTypeSelected: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        this.value = false
    }

    var isLiveTypeSelected: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
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