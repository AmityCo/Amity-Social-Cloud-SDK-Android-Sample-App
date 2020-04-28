package com.ekoapp.sample.core.base.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class DisposableViewModel : ViewModel() {
    protected val disposables by lazy { CompositeDisposable() }
    override fun onCleared() {
        disposables.clear()
    }
}