package com.ekoapp.sample.chatfeature.channellist.filter.includetags

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IncludeTagFilterViewModel : ViewModel() {

    var includingTags: MutableLiveData<String> = MutableLiveData<String>().apply {
        this.value  = ""
    }

}