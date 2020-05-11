package com.ekoapp.sample.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.rx.into
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterViewModel @Inject constructor(val context: Context, private val registerRepository: RegisterRepository) : DisposableViewModel() {
    private val registerActionRelay = MutableLiveData<Unit>()

    fun observeRegisterAction(): LiveData<Unit> = registerActionRelay

    fun register(displayName: String) {
        registerRepository.registerEkoClient(displayName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    registerActionRelay.postValue(Unit)
                }
                .subscribe() into disposables
    }

    fun getDisplayName(): String = if (EkoClient.getDisplayName().isNullOrEmpty()) EkoClient.getUserId() else EkoClient.getDisplayName()

}