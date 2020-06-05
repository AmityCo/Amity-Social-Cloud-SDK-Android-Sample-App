package com.ekoapp.sample.chatfeature.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import javax.inject.Inject

class ChannelSettingsViewModel @Inject constructor(private val context: Context) : DisposableViewModel() {

    var selectedMembership: MutableLiveData<String> = MutableLiveData<String>().apply {
        this.value = ""
    }

    fun getChannelTypes(): ArrayList<String> {
        val items = ArrayList<String>()
        items.add(context.getString(R.string.temporarily_standard))
        items.add(context.getString(R.string.temporarily_private))
        items.add(context.getString(R.string.temporarily_broadcast))
        items.add(context.getString(R.string.temporarily_conversation))
        return items
    }
}