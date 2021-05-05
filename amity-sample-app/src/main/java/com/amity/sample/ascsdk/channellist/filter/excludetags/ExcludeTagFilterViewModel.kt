package com.amity.sample.ascsdk.channellist.filter.excludetags

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExcludeTagFilterViewModel : ViewModel() {

    var excludingTags: MutableLiveData<String> = MutableLiveData<String>().apply {
        this.value  = ""
    }

}