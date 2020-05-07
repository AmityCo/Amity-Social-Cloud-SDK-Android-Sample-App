package com.ekoapp.sample.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.rx.into
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class RegisterViewModel @Inject constructor() : DisposableViewModel() {

    private val registerActionRelay = MutableLiveData<Unit>()

    fun registerRelay(): LiveData<Unit> = registerActionRelay

    fun register(displayName: String) {
        EkoClient.registerDevice(EkoClient.getUserId(), displayName)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    registerActionRelay.postValue(Unit)
                }
                .subscribe() into disposables
    }

    fun getDisplayName(): String = if (EkoClient.getDisplayName().isNullOrEmpty()) EkoClient.getUserId() else EkoClient.getDisplayName()

}