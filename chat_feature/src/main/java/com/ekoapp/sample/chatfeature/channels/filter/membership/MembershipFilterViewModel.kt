package com.ekoapp.sample.chatfeature.channels.filter.membership

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MembershipFilterViewModel : ViewModel() {

    var selectedMembership: MutableLiveData<String> = MutableLiveData<String>().apply {
        this.value = ""
    }

}