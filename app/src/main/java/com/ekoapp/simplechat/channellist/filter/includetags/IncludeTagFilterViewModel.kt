package com.ekoapp.simplechat.channellist.filter.includetags

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IncludeTagFilterViewModel : ViewModel() {

    var includingTags: MutableLiveData<String> = MutableLiveData<String>().apply {
        this.value  = ""
    }

}