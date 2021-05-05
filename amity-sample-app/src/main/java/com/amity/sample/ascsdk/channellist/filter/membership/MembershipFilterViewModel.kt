package com.amity.sample.ascsdk.channellist.filter.membership

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MembershipFilterViewModel : ViewModel() {

    var selectedMembership: MutableLiveData<String> = MutableLiveData<String>().apply {
        this.value = ""
    }

}